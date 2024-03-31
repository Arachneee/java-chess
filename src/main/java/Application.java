import connection.ChessConnectionGenerator;
import controller.ChessFrontController;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(final String[] args) {
        final Connection connection = ChessConnectionGenerator.getConnection();

        final ChessFrontController chessFrontController = new ChessFrontController(connection);
        chessFrontController.run();

        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException e) {
                throw new RuntimeException("서버 오류입니다.");
            }
        }
    }
}
