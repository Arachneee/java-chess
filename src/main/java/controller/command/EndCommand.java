package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.game.ChessGame;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class EndCommand extends RunningCommand {

    public EndCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameNumber) throws SQLException {
        chessGameService().endGame(gameNumber);

        final ChessGame chessGame = chessGameService().findGameByNumber(gameNumber);
        OutputView.printStatus(chessGame.getChessResult());

        return new StartingStatus();
    }
}



