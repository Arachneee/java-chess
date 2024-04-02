package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import controller.status.StartingStatus;
import domain.game.ChessGameStatus;
import domain.square.Square;
import dto.ChessGameDto;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class MoveCommand extends RunningCommand {

    private static final int SOURCE_INDEX = 0;
    private static final int TARGET_INDEX = 1;
    private static final int MOVE_COMMAND_SIZE = 2;

    public MoveCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus execute(final List<String> command, final int gameNumber) throws SQLException {
        validateCommand(command);
        final Square source = Square.from(command.get(SOURCE_INDEX));
        final Square target = Square.from(command.get(TARGET_INDEX));

        chessGameService().move(gameNumber, source, target);

        final ChessGameDto chessGame = chessGameService().getGameDto(gameNumber);
        OutputView.printChessBoard(chessGame.board());

        if (chessGame.status() == ChessGameStatus.END) {
            OutputView.printStatus(chessGame.chessResult());
            return new StartingStatus();
        }
        return new RunningStatus(gameNumber, chessGameService());
    }

    private void validateCommand(final List<String> command) {
        if (command.size() != MOVE_COMMAND_SIZE) {
            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }
}
