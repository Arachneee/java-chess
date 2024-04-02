package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import dto.ChessGameDto;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class EndCommand extends RunningCommand {

    public EndCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus execute(final List<String> playCommandFormat, final int gameNumber) throws SQLException {
        chessGameService().endGame(gameNumber);

        final ChessGameDto chessGame = chessGameService().getGameDto(gameNumber);

        OutputView.printStatus(chessGame.chessResult());

        return new StartingStatus();
    }
}



