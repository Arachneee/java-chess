package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.game.ChessGame;
import service.ChessGameService;

import java.util.List;

public class StatusCommand extends RunningCommand {

    public StatusCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameId) {
        final ChessGame chessGame = chessGameService().findGameById(gameId);

        printScoreStatus(gameId);

        return new RunningStatus(chessGame);
    }
}
