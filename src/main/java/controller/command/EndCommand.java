package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.result.ChessResult;
import dto.ChessGameDto;
import service.ChessGameService;
import service.ChessResultService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class EndCommand extends RunningCommand {

    public EndCommand(final ChessGameService chessGameService, final ChessResultService chessResultService) {
        super(chessGameService, chessResultService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameNumber) throws SQLException {
        chessGameService().endGame(gameNumber);

        final ChessGameDto gameDto = chessGameService().getGameDto(gameNumber);
        final ChessResult chessResult = chessResultService().calculateChessResult(gameDto.board());

        chessResultService().saveResult(gameNumber, chessResult);
        OutputView.printStatus(chessResult);

        return new StartingStatus();
    }
}



