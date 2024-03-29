package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.game.ChessGame;
import domain.result.ChessGameResult;
import service.ChessGameService;
import view.OutputView;

import java.util.List;

public class StatusCommand implements Command {

    private final ChessGameService chessGameService;

    public StatusCommand(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStart() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public ChessProgramStatus executePlay(final List<String> playCommandFormat, final int gameId) {
        final ChessGame chessGame = chessGameService.findGameById(gameId);

        final ChessGameResult chessGameResult = chessGame.calculateResult();
        OutputView.printStatus(chessGameResult);

        return new RunningStatus(chessGame);
    }
}
