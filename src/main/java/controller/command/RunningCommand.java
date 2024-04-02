package controller.command;

import controller.status.ChessProgramStatus;
import service.ChessGameService;

import java.sql.SQLException;
import java.util.List;

public abstract class RunningCommand {

    private final ChessGameService chessGameService;

    protected RunningCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    public abstract ChessProgramStatus execute(final List<String> command, final int gameNumber) throws SQLException;

    protected ChessGameService chessGameService() {
        return chessGameService;
    }
}
