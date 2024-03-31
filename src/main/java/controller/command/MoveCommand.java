package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import controller.status.StartingStatus;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.square.Square;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class MoveCommand extends RunningCommand {

    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;
    private static final int MOVE_COMMAND_SIZE = 3;

    public MoveCommand(final ChessGameService chessGameService) {
        super(chessGameService);
    }

    @Override
    public ChessProgramStatus executeRunning(final List<String> command, final int gameNumber) {
        validateCommand(command);
        final Square source = Square.from(command.get(SOURCE_INDEX));
        final Square target = Square.from(command.get(TARGET_INDEX));

        try {
            chessGameService().move(gameNumber, source, target);
        } catch (final SQLException e) {
            throw new RuntimeException("서버 오류입니다.");
        }

        final ChessGame chessGame = chessGameService().findGameByNumber(gameNumber);
        printChessBoard(chessGame);

        if (chessGame.getStatus() == ChessGameStatus.END) {
            OutputView.printStatus(chessGame.getChessResult());
            return new StartingStatus();
        }
        return new RunningStatus(chessGame);
    }

    private void validateCommand(final List<String> command) {
        if (command.size() != MOVE_COMMAND_SIZE) {
            throw new IllegalArgumentException("잘못된 command입니다.");
        }
    }

    private static void printChessBoard(final ChessGame chessGame) {
        final ChessBoard chessBoard = chessGame.getChessBoard();
        OutputView.printChessBoard(chessBoard.getPieceSquares());
    }
}
