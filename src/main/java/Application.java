import connection.ChessConnectionGenerator;
import controller.ChessFrontController;
import view.OutputView;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(final String[] args) {
        try (final Connection connection = ChessConnectionGenerator.getConnection()) {
            final ChessFrontController chessFrontController = new ChessFrontController(connection);
            chessFrontController.run();
        } catch (final SQLException e) {
            OutputView.printError("서버 오류입니다.");
        }
    }
}
