package controller.status;

import dto.ChessGameDto;
import service.ChessGameService;
import view.InputView;
import view.format.CommandFormat;

public class RunningStatus implements ChessProgramStatus {

    private final int gameNumber;
    private final ChessGameService chessGameService;

    public RunningStatus(final int gameNumber, final ChessGameService chessGameService) {
        this.gameNumber = gameNumber;
        this.chessGameService = chessGameService;
    }

    @Override
    public CommandFormat readCommand() {
        final ChessGameDto chessGameDto = chessGameService.getGameDto(gameNumber);

        return InputView.readGameCommand(chessGameDto.currentTeam(), chessGameDto.currentPlayerName());
    }

    @Override
    public int getGameNumber() {
        return gameNumber;
    }

    @Override
    public boolean isNotEnd() {
        return true;
    }

    @Override
    public boolean isStarting() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
