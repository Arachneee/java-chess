import connection.ChessConnectionGenerator;
import controller.ChessFrontController;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(final String[] args) throws SQLException {
        final Connection connection = ChessConnectionGenerator.getConnection();

        final ChessFrontController chessFrontController = new ChessFrontController(connection);
        chessFrontController.run();

        if (connection != null) {
            connection.close();
        }
    }
}
