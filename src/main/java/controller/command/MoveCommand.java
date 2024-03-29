package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import controller.status.StartingStatus;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.result.ChessGameResult;
import domain.square.Square;
import service.ChessGameService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class MoveCommand implements Command {

    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    private final ChessGameService chessGameService;

    public MoveCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStart() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public ChessProgramStatus executePlay(final List<String> command, final int gameId) throws SQLException {
        validateCommand(command);
        final Square source = Square.from(command.get(SOURCE_INDEX));
        final Square target = Square.from(command.get(TARGET_INDEX));

        chessGameService.move(gameId, source, target);
        final ChessGame chessGame = chessGameService.findGameById(gameId);

        final ChessBoard chessBoard = chessGame.getChessBoard();
        OutputView.printChessBoard(chessBoard.getPieceSquares());

        if (chessGame.getStatus() == ChessGameStatus.END) {
            final ChessGameResult chessGameResult = chessGameService.calculateResult(gameId);
            OutputView.printStatus(chessGameResult);

            return new StartingStatus();
        }

        return new RunningStatus(chessGame);
    }

    private void validateCommand(final List<String> command) {
        if (command.size() != 3) {
            throw new IllegalArgumentException("잘못된 command입니다.");
        }
    }
}
