package controller.status;

public interface ChessProgramStatus {

    String readCommand();

    int getGameNumber();

    boolean isNotEnd();

    boolean isStarting();

    boolean isRunning();
}
