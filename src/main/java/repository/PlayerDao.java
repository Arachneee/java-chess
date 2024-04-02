package repository;

import domain.player.Player;
import domain.player.PlayerName;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerDao {

    private final Connection connection;

    public PlayerDao(final Connection connection) {
        this.connection = connection;
    }

    public Player add(final PlayerName name) {
        final var query = "INSERT INTO player VALUES(null, ?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name.getName());

            preparedStatement.executeUpdate();

            return new Player(name);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Player> findPlayerByName(final String name) {
        final var query = "SELECT name FROM player WHERE name = ?";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Player(new PlayerName(resultSet.getString("name"))));
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public boolean exist(final PlayerName name) {
        final var query = "SELECT * FROM player WHERE name = ?";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name.getName());
            final var resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlayerName> findAllNames() {
        final var query = "SELECT * FROM player";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();

            final List<PlayerName> playerNames = new ArrayList<>();

            while (resultSet.next()) {
                playerNames.add(new PlayerName(resultSet.getString("name")));
            }

            return playerNames;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
