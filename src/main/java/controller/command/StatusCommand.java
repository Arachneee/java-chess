package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import dto.ChessGameDto;
import service.ChessGameService;
import view.OutputView;

import java.util.List;

public class StatusCommand extends RunningCommand {

    public StatusCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus execute(final List<String> playCommandFormat, final int gameNumber) {
        final ChessGameDto chessGame = chessGameService().getGameDto(gameNumber);

        OutputView.printStatus(chessGame.chessResult());

        return new RunningStatus(gameNumber, chessGameService());
    }
}
