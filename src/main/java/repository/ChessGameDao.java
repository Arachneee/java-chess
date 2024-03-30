package repository;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.player.Player;
import domain.player.PlayerName;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessGameDao {

    private final Connection connection;
    private final ChessBoardDao chessBoardDao;

    public ChessGameDao(final Connection connection) {
        this.connection = connection;
        this.chessBoardDao = new ChessBoardDao(connection);
    }

    public void create(final ChessGame chessGame) {
        final var query = "INSERT INTO game (current_team, status, black_player_id, white_player_id) VALUES(?, ?, " +
                "(SELECT id FROM player WHERE name = ?), " +
                "(SELECT id FROM player WHERE name = ?))";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, chessGame.getCurrentTeam().name());
            preparedStatement.setString(2, chessGame.getStatus().name());
            preparedStatement.setString(3, chessGame.getBlackPlayer().getName());
            preparedStatement.setString(4, chessGame.getWhitePlayer().getName());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        chessBoardDao.addBoard(chessGame.getChessBoard(), chessGame.getId());
    }

    public Optional<ChessGame> findById(final int id) {
        final ChessBoard chessBoard = chessBoardDao.findByGameId(id);

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

    public Optional<ChessGame> findByIdAndStatus(final int id, final ChessGameStatus chessGameStatus) {
        final ChessBoard chessBoard = chessBoardDao.findByGameId(id);

        final var query = "SELECT * FROM game AS G " +
                "LEFT JOIN player AS BP " +
                "ON G.black_player_id = BP.id " +
                "LEFT JOIN player AS WP " +
                "ON G.white_player_id = WP.id " +
                "WHERE G.id = (?) AND G.status = '" + chessGameStatus.name() + "'";
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

    public List<Integer> findIdsByStatus(final ChessGameStatus chessGameStatus) {
        final var query = "SELECT id FROM game WHERE status = '" + chessGameStatus.name() + "'";
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

    public int findAutoIncrement() {
        final var query = "SELECT AUTO_INCREMENT " +
                "FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() " +
                "AND table_name = 'game'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public void update(final ChessGame chessGame) {
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
