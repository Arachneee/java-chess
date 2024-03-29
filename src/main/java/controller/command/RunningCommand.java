package controller.command;

import controller.status.ChessProgramStatus;
import domain.result.ChessGameResult;
import service.ChessGameService;
import view.OutputView;

public abstract class RunningCommand implements Command {

    private final ChessGameService chessGameService;

    protected RunningCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStarting() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public boolean isStarting() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    protected void printScoreStatus(final int gameId) {
        final ChessGameResult chessGameResult = chessGameService.calculateResult(gameId);
        OutputView.printStatus(chessGameResult);
    }

    protected ChessGameService chessGameService() {
        return chessGameService;
    }
}
