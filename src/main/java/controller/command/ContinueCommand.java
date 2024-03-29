package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.player.Player;
import service.ChessGameService;
import view.InputView;
import view.OutputView;

import java.util.List;

public class ContinueCommand implements Command {

    private final ChessGameService chessGameService;

    public ContinueCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStart() {
        final List<Integer> runningGame = chessGameService.findRunningGameIds();
        final int gameId = readGameId(runningGame);

        final ChessGame chessGame = chessGameService.findGameById(gameId);

        final Player blackPlayer = chessGame.getBlackPlayer();
        final Player whitePlayer = chessGame.getWhitePlayer();
        final ChessBoard chessBoard = chessGame.getChessBoard();

        OutputView.printGameOption(gameId, blackPlayer.getName(), whitePlayer.getName());
        OutputView.printChessBoard(chessBoard.getPieceSquares());

        return new RunningStatus(chessGame);
    }

    @Override
    public ChessProgramStatus executePlay(final List<String> command, final int gameId) {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    private int readGameId(final List<Integer> runningGame) {
        while (true) {
            final int input = InputView.readContinueGame(runningGame);
            final boolean hasGame = chessGameService.isRunningGame(input);
            if (hasGame) {
                return input;
            }
            OutputView.printError("게임 ID가 존재하지 않습니다.");
        }
    }
}
