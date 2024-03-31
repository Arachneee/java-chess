package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.game.ChessGame;
import service.ChessGameService;
import view.OutputView;

import java.util.List;

public class StatusCommand extends RunningCommand {

    public StatusCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameNumber) {
        final ChessGame chessGame = chessGameService().findRunningGameByNumber(gameNumber);

        OutputView.printStatus(chessGame.getChessResult());

        return new RunningStatus(chessGame);
    }
}
