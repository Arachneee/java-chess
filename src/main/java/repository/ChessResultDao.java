package repository;

import domain.WinStatus;
import domain.player.PlayerName;
import domain.result.ChessResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChessResultDao {

    private final Connection connection;

    public ChessResultDao(final Connection connection) {
        this.connection = connection;
    }

    public void create(final ChessResult chessResult, final int gameNumber) {
        final var query = "INSERT INTO result VALUES(null, " +
                "(SELECT id FROM game WHERE number = ?)," +
                "(SELECT black_player_id FROM game WHERE number = ?), " +
                "(SELECT white_player_id FROM game WHERE number = ?), " +
                "?, ?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameNumber);
            preparedStatement.setInt(2, gameNumber);
            preparedStatement.setInt(3, gameNumber);
            preparedStatement.setDouble(4, chessResult.getBlackScore());
            preparedStatement.setDouble(5, chessResult.getWhiteScore());
            preparedStatement.setString(6, chessResult.getWinStatus().name());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WinStatus> findBlackWinStatus(final PlayerName player) {
        final var query = "SELECT win_status FROM result AS R " +
                "LEFT JOIN player AS P ON R.black_player_id = P.id " +
                "WHERE P.name = (?)";

        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, player.getName());

            final var resultSet = preparedStatement.executeQuery();

            final List<WinStatus> statuses = new ArrayList<>();

            while (resultSet.next()) {
                statuses.add(WinStatus.valueOf(resultSet.getString("win_status")));
            }
            return statuses;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WinStatus> findWhiteWinStatus(final PlayerName player) {
        final var query = "SELECT win_status FROM result AS R " +
                "LEFT JOIN player AS P ON R.white_player_id = P.id " +
                "WHERE P.name = (?)";

        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, player.getName());

            final var resultSet = preparedStatement.executeQuery();

            final List<WinStatus> statuses = new ArrayList<>();

            while (resultSet.next()) {
                statuses.add(WinStatus.valueOf(resultSet.getString("win_status")));
            }
            return statuses;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
