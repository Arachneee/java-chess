package controller.status;

import domain.Team;
import domain.game.ChessGame;
import domain.player.Player;
import view.InputView;

public class RunningStatus implements ChessProgramStatus {

    private final ChessGame chessGame;


    public RunningStatus(final ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    @Override
    public String readCommand() {
        final Team currentTeam = chessGame.getCurrentTeam();
        final Player currentPlayer = chessGame.getCurrentPlayer();

        return InputView.readGameCommand(currentTeam, currentPlayer.getName());
    }

    @Override
    public int getGameId() {
        return chessGame.getId();
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
