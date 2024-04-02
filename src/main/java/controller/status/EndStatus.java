package controller.status;

import view.format.CommandFormat;

public class EndStatus implements ChessProgramStatus {

    @Override
    public CommandFormat readCommand() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public int getGameNumber() {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    @Override
    public boolean isNotEnd() {
        return false;
    }

    @Override
    public boolean isStarting() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
