package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import dto.ChessGameDto;
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
        final List<Integer> runningGameNumbers = chessGameService.findRunningGameNumbers();
        final int gameNumber = readGameNumber(runningGameNumbers);

        final ChessGameDto chessGame = chessGameService.getGameDto(gameNumber);
        OutputView.printStartGame(chessGame);

        return new RunningStatus(chessGame.gameNumber(), chessGameService);
    }

    private int readGameNumber(final List<Integer> runningGameNumbers) {
        while (true) {
            try {
                final int input = InputView.readContinueGame(runningGameNumbers);
                if (runningGameNumbers.contains(input)) {
                    return input;
                }
                throw new IllegalArgumentException("실행중이지 않는 게임번호입니다.");
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
