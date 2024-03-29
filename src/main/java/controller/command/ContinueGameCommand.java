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
        final int gameId = readGameId(runningGame);

        final ChessGame chessGame = chessGameService.findGameById(gameId);
        printStartGame(chessGame);

        return new RunningStatus(chessGame);
    }

    private int readGameId(final List<Integer> runningGame) {
        while (true) {
            final int input = InputView.readContinueGame(runningGame);
            final boolean hasGame = chessGameService.isRunningGame(input);
            if (hasGame) {
                return input;
            }
            OutputView.printError("게임 ID가 존재하지 않습니다.");
        }
    }
}
