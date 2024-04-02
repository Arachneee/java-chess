package config;

import connection.ChessConnectionGenerator;
import controller.ChessFrontController;
import repository.ChessGameRepository;
import repository.ChessResultDao;
import repository.PlayerDao;
import service.ChessGameService;
import service.PlayerService;

import java.sql.Connection;
import java.sql.SQLException;

public class AppConfig {

    private static Connection connection;

    private AppConfig() {
    }

    public static ChessFrontController chessFrontController() throws SQLException {
        return new ChessFrontController(playerService(), chessGameService());
    }

    public static Connection connection() throws SQLException {
        if (connection == null) {
            connection = ChessConnectionGenerator.getConnection();
        }
        return connection;
    }

    public static void connectionClose() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private static PlayerService playerService() throws SQLException {
        return new PlayerService(playerDao());
    }

    private static PlayerDao playerDao() throws SQLException {
        return new PlayerDao(connection());
    }

    private static ChessGameService chessGameService() throws SQLException {
        return new ChessGameService(chessGameRepository(), chessResultDao());
    }

    private static ChessGameRepository chessGameRepository() throws SQLException {
        return new ChessGameRepository(connection());
    }

    private static ChessResultDao chessResultDao() throws SQLException {
        return new ChessResultDao(connection());
    }
}
