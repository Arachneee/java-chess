package domain.game.command;

import domain.game.ChessGame;

import java.util.function.Consumer;

public class EndCommand implements ChessCommand {

    @Override
    public void execute(final ChessGame chessGame, final Consumer<ChessGame> callBack) {
        chessGame.end();
    }
}