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
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameNumber) {
        try {
            chessGameService().endGame(gameNumber);
        } catch (final SQLException e) {
            throw new RuntimeException("서버 오류입니다.");
        }

        final ChessGame chessGame = chessGameService().findGameByNumber(gameNumber);
        OutputView.printStatus(chessGame.getChessResult());

        return new StartingStatus();
    }
}



