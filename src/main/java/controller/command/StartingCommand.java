package controller.command;

import controller.status.ChessProgramStatus;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.player.Player;
import view.OutputView;

import java.util.List;

public abstract class StartingCommand implements Command {

    @Override
    public ChessProgramStatus executeRunning(final List<String> command, final int gameId) {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public boolean isStarting() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    protected void printStartGame(final ChessGame chessGame) {
        final Player blackPlayer = chessGame.getBlackPlayer();
        final Player whitePlayer = chessGame.getWhitePlayer();
        final ChessBoard chessBoard = chessGame.getChessBoard();

        OutputView.printGameOption(chessGame.getId(), blackPlayer.getName(), whitePlayer.getName());
        OutputView.printChessBoard(chessBoard.getPieceSquares());
    }
}
