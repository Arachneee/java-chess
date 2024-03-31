package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.result.ChessResult;
import dto.ChessGameDto;
import service.ChessGameService;
import service.ChessResultService;
import view.OutputView;

import java.util.List;

public class StatusCommand extends RunningCommand {

    public StatusCommand(final ChessGameService chessGameService, final ChessResultService chessResultService) {
        super(chessGameService, chessResultService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> playCommandFormat, final int gameNumber) {
        final ChessGameDto chessGame = chessGameService().getGameDto(gameNumber);

        final ChessResult chessResult = chessResultService().calculateChessResult(chessGame.board());
        OutputView.printStatus(chessResult);

        return new RunningStatus(gameNumber, chessGameService());
    }
}
