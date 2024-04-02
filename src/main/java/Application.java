import config.AppConfig;
import controller.ChessFrontController;
import view.OutputView;

public class Application {

    public static void main(final String[] args) {
        try {
            final ChessFrontController chessFrontController = AppConfig.chessFrontController();
            chessFrontController.run();

            AppConfig.connectionClose();
        } catch (final Exception e) {
            OutputView.printError("서버 오류입니다.");
        }
    }
}
