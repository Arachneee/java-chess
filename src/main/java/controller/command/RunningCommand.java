package controller.command;

import controller.status.ChessProgramStatus;
import service.ChessGameService;

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

    protected ChessGameService chessGameService() {
        return chessGameService;
    }
}
