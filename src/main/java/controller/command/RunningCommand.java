package controller.command;

import controller.status.ChessProgramStatus;
import service.ChessGameService;
import service.ChessResultService;

public abstract class RunningCommand implements Command {

    private final ChessGameService chessGameService;
    private final ChessResultService chessResultService;

    protected RunningCommand(final ChessGameService chessGameService, final ChessResultService chessResultService) {
        this.chessGameService = chessGameService;
        this.chessResultService = chessResultService;
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

    protected ChessGameService chessGameService() {
        return chessGameService;
    }

    protected ChessResultService chessResultService() {
        return chessResultService;
    }
}
