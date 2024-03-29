package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.game.ChessGame;
import service.ChessGameService;
import view.InputView;
import view.OutputView;

import java.util.List;

public class ContinueGameCommand extends StartingCommand {

    private final ChessGameService chessGameService;

    public ContinueGameCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStarting() {
        final List<Integer> runningGame = chessGameService.findRunningGameIds();
        final ChessGame chessGame = findRunningGame(runningGame);
        printStartGame(chessGame);

        return new RunningStatus(chessGame);
    }

    private ChessGame findRunningGame(final List<Integer> runningGame) {
        while (true) {
            try {
                final int input = InputView.readContinueGame(runningGame);
                return chessGameService.findRunningGameById(input);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
