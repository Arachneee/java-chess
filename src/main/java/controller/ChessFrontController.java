package controller;

import controller.status.ChessControllerStatus;
import controller.status.ChessStartingController;
import service.ChessGameService;
import service.PlayerService;
import view.OutputView;
import view.format.CommandFormat;

public class ChessFrontController {

    private ChessControllerStatus status;

    public ChessFrontController(final PlayerService playerService, final ChessGameService chessGameService) {
        this.status = new ChessStartingController(playerService, chessGameService);
    }

    public void run() throws Exception {
        while (true) {
            try {
                final CommandFormat command = status.readCommand();
                if (command == CommandFormat.QUIT) {
                    return;
                }
                status = status.execute(command);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
