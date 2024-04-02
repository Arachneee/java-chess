package controller.status;

import view.format.CommandFormat;

public interface ChessProgramStatus {

    CommandFormat readCommand();

    int getGameNumber();

    boolean isNotEnd();

    boolean isStarting();

    boolean isRunning();
}
