package controller.command;

import controller.status.ChessProgramStatus;

import java.util.List;

public abstract class StartingCommand implements Command {

    @Override
    public ChessProgramStatus executeRunning(final List<String> command, final int gameNumber) {
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
}
