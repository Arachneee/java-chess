package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import controller.status.StartingStatus;
import domain.game.ChessGameStatus;
import domain.result.ChessResult;
import domain.square.Square;
import dto.ChessGameDto;
import service.ChessGameService;
import service.ChessResultService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class MoveCommand extends RunningCommand {

    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;
    private static final int MOVE_COMMAND_SIZE = 3;

    public MoveCommand(final ChessGameService chessGameService, final ChessResultService chessResultService) {
        super(chessGameService, chessResultService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> command, final int gameNumber) throws SQLException {
        validateCommand(command);
        final Square source = Square.from(command.get(SOURCE_INDEX));
        final Square target = Square.from(command.get(TARGET_INDEX));

        chessGameService().move(gameNumber, source, target);

        final ChessGameDto chessGame = chessGameService().getGameDto(gameNumber);
        OutputView.printChessBoard(chessGame.board());

        if (chessGame.status() == ChessGameStatus.END) {
            final ChessResult chessResult = chessResultService().calculateChessResult(chessGame.board());
            chessResultService().saveResult(gameNumber, chessResult);

            OutputView.printStatus(chessResult);
            return new StartingStatus();
        }
        return new RunningStatus(gameNumber, chessGameService());
    }

    private void validateCommand(final List<String> command) {
        if (command.size() != MOVE_COMMAND_SIZE) {
            throw new IllegalArgumentException("잘못된 command입니다.");
        }
    }
}
