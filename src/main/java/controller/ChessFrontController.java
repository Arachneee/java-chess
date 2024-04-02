package controller;

import controller.status.ChessControllerStatus;
import controller.status.ChessStartingController;
import service.ChessGameService;
import service.PlayerService;
import view.OutputView;
import view.format.Command;
import view.format.CommandFormat;

import java.sql.SQLException;

public class ChessFrontController {

    private ChessControllerStatus status;

    public ChessFrontController(final PlayerService playerService, final ChessGameService chessGameService) {
        this.status = new ChessStartingController(playerService, chessGameService);
    }

    public void run() {
        while (true) {
            try {
                final Command command = status.readCommand();
                if (command.getCommandFormat() == CommandFormat.QUIT) {
                    return;
                }
                status = status.execute(command);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            } catch (final SQLException e) {
                OutputView.printError("서버 오류입니다.");
                return;
            }
        }
    }
}
