package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.result.ChessGameResult;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class EndCommand implements Command {

    private final ChessGameService chessGameService;

    public EndCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStart() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public ChessProgramStatus executePlay(final List<String> playCommandFormat, final int gameId) throws SQLException {
        chessGameService.endGame(gameId);

        final ChessGameResult chessGameResult = chessGameService.calculateResult(gameId);
        OutputView.printStatus(chessGameResult);

        return new StartingStatus();
    }
}



