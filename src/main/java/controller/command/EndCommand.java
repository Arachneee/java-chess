package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import service.ChessGameService;

import java.sql.SQLException;
import java.util.List;

public class EndCommand extends RunningCommand {

    public EndCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameId) throws SQLException {
        chessGameService().endGame(gameId);

        printScoreStatus(gameId);

        return new StartingStatus();
    }
}



