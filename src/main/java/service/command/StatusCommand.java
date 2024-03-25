package service.command;

import service.ChessGame;

import java.util.function.BiConsumer;

public class StatusCommand implements ChessCommand {
    @Override
    public void execute(final ChessGame chessGame, final BiConsumer<ChessGame, Boolean> callBack) {
        callBack.accept(chessGame, true);
    }
}