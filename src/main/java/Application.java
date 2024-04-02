import connection.ChessConnectionGenerator;
import controller.ChessFrontController;
import service.ChessGameService;
import service.PlayerService;
import view.OutputView;

import java.sql.Connection;

public class Application {

    public static void main(final String[] args) {
        try (final Connection connection = ChessConnectionGenerator.getConnection()) {
            final PlayerService playerService = new PlayerService(connection);
            final ChessGameService chessGameService = new ChessGameService(connection);

            final ChessFrontController chessFrontController = new ChessFrontController(playerService, chessGameService);
            chessFrontController.run();
        } catch (final Exception e) {
            OutputView.printError("서버 오류입니다.");
        }
    }
}
