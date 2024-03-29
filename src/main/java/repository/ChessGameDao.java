package repository;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.piece.Piece;
import domain.piece.PieceMaker;
import domain.player.Player;
import domain.player.PlayerName;
import domain.square.Square;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ChessGameDao {

    private final Connection connection;

    public ChessGameDao(final Connection connection) {
        this.connection = connection;
    }

    public int createGame(
            final Player blackPlayer,
            final Player whitePlayer,
            final Team currentTeam,
            final ChessGameStatus status
    ) {
        final var query = "INSERT INTO game (current_team, status, black_player_id, white_player_id) VALUES(?, ?, " +
                "(SELECT id FROM player WHERE name = ?), " +
                "(SELECT id FROM player WHERE name = ?))";
        try (final var preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currentTeam.name());
            preparedStatement.setString(2, status.name());
            preparedStatement.setString(3, blackPlayer.getName());
            preparedStatement.setString(4, whitePlayer.getName());

            preparedStatement.executeUpdate();

            try (final ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ChessGame> findGameById(final int id) {
        final ChessBoard chessBoard = findChessBoard(id);

        final var query = "SELECT * FROM game AS G " +
                "LEFT JOIN player AS BP " +
                "ON G.black_player_id = BP.id " +
                "LEFT JOIN player AS WP " +
                "ON G.white_player_id = WP.id " +
                "WHERE G.id = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(ChessGame.ChessGameBuilder.builder()
                        .id(resultSet.getInt("G.id"))
                        .blackPlayer(new Player(new PlayerName(resultSet.getString("BP.name"))))
                        .whitePlayer(new Player(new PlayerName(resultSet.getString("WP.name"))))
                        .chessBoard(chessBoard)
                        .status(ChessGameStatus.valueOf(resultSet.getString("G.status")))
                        .currentTeam(Team.valueOf(resultSet.getString("G.current_team")))
                        .build()
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private ChessBoard findChessBoard(final int gameId) {
        final var query = "SELECT * FROM board WHERE game_id = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);

            final var resultSet = preparedStatement.executeQuery();

            final Map<Square, Piece> squarePieces = new HashMap<>();

            while (resultSet.next()) {
                squarePieces.put(Square.of(
                                resultSet.getString("file"),
                                resultSet.getString("rank")
                        ),
                        PieceMaker.of(
                                resultSet.getString("piece_type"),
                                resultSet.getString("team")
                        ));
            }
            return new ChessBoard(squarePieces);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ChessGame> findRunningGameById(final int id) {
        final ChessBoard chessBoard = findRunningChessBoard(id);

        final var query = "SELECT * FROM game AS G " +
                "LEFT JOIN player AS BP " +
                "ON G.black_player_id = BP.id " +
                "LEFT JOIN player AS WP " +
                "ON G.white_player_id = WP.id " +
                "WHERE G.id = (?) AND G.status = 'RUNNING'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(ChessGame.ChessGameBuilder.builder()
                        .id(resultSet.getInt("G.id"))
                        .blackPlayer(new Player(new PlayerName(resultSet.getString("BP.name"))))
                        .whitePlayer(new Player(new PlayerName(resultSet.getString("WP.name"))))
                        .chessBoard(chessBoard)
                        .status(ChessGameStatus.valueOf(resultSet.getString("G.status")))
                        .currentTeam(Team.valueOf(resultSet.getString("G.current_team")))
                        .build()
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private ChessBoard findRunningChessBoard(final int id) {
        final var query = "SELECT * FROM game AS G " +
                "RIGHT JOIN board AS B ON G.id = B.game_id " +
                "WHERE G.id = (?) AND G.status = 'RUNNING'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            final var resultSet = preparedStatement.executeQuery();

            final Map<Square, Piece> squarePieces = new HashMap<>();

            while (resultSet.next()) {
                squarePieces.put(Square.of(
                                resultSet.getString("B.file"),
                                resultSet.getString("B.rank")
                        ),
                        PieceMaker.of(
                                resultSet.getString("B.piece_type"),
                                resultSet.getString("B.team")
                        ));
            }
            return new ChessBoard(squarePieces);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> findRunningGameIds() {
        final var query = "SELECT id FROM game WHERE status = 'RUNNING'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();

            final List<Integer> ids = new ArrayList<>();

            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
            return ids;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateChessGame(final ChessGame chessGame) {
        final var query = "UPDATE game SET " +
                "current_team = (?), status = (?) " +
                "WHERE id = (?)";

        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, chessGame.getCurrentTeam().name());
            preparedStatement.setString(2, chessGame.getStatus().name());
            preparedStatement.setInt(3, chessGame.getId());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
