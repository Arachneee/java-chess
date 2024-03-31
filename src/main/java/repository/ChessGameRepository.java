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

public class ChessGameRepository {

    private final Connection connection;
    private final ChessBoardDao chessBoardDao;

    public ChessGameRepository(final Connection connection) {
        this.connection = connection;
        this.chessBoardDao = new ChessBoardDao(connection);
    }

    public void create(final ChessGame chessGame) {
        final var query = "INSERT INTO game (number, current_team, status, black_player_id, white_player_id) " +
                "VALUES(?, ?, ?, " +
                "(SELECT id FROM player WHERE name = ?), " +
                "(SELECT id FROM player WHERE name = ?))";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, chessGame.getNumber());
            preparedStatement.setString(2, chessGame.getCurrentTeam().name());
            preparedStatement.setString(3, chessGame.getStatus().name());
            preparedStatement.setString(4, chessGame.getBlackPlayer().getName());
            preparedStatement.setString(5, chessGame.getWhitePlayer().getName());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        chessBoardDao.addBoard(chessGame.getChessBoard(), chessGame.getNumber());
    }

    public Optional<ChessGame> findByNumber(final int number) {
        final ChessBoard chessBoard = chessBoardDao.findByGameNumber(number);

        final var query = "SELECT * FROM game AS G " +
                "LEFT JOIN player AS BP " +
                "ON G.black_player_id = BP.id " +
                "LEFT JOIN player AS WP " +
                "ON G.white_player_id = WP.id " +
                "WHERE G.number = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, number);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(ChessGame.ChessGameBuilder.builder()
                        .number(resultSet.getInt("G.number"))
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

    public Optional<ChessGame> findByNumberAndStatus(final int number, final ChessGameStatus chessGameStatus) {
        final ChessBoard chessBoard = chessBoardDao.findByGameNumber(number);

        final var query = "SELECT * FROM game AS G " +
                "LEFT JOIN player AS BP " +
                "ON G.black_player_id = BP.id " +
                "LEFT JOIN player AS WP " +
                "ON G.white_player_id = WP.id " +
                "WHERE G.number = (?) AND G.status = '" + chessGameStatus.name() + "'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, number);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(ChessGame.ChessGameBuilder.builder()
                        .number(resultSet.getInt("G.number"))
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

    public List<Integer> findNumbersByStatus(final ChessGameStatus chessGameStatus) {
        final var query = "SELECT number FROM game WHERE status = '" + chessGameStatus.name() + "'";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();

            final List<Integer> numbers = new ArrayList<>();

            while (resultSet.next()) {
                numbers.add(resultSet.getInt("number"));
            }
            return numbers;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int findMaxNumber() {
        final var query = "SELECT MAX(number) FROM game";
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
                "WHERE number = (?)";

        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, chessGame.getCurrentTeam().name());
            preparedStatement.setString(2, chessGame.getStatus().name());
            preparedStatement.setInt(3, chessGame.getNumber());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        chessBoardDao.deleteByGameNumber(chessGame.getNumber());
        chessBoardDao.addBoard(chessGame.getChessBoard(), chessGame.getNumber());
    }

    public void updateStatus(final ChessGame chessGame) {
        final var query = "UPDATE game SET " +
                "status = (?) " +
                "WHERE number = (?)";

        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, chessGame.getStatus().name());
            preparedStatement.setInt(2, chessGame.getNumber());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
