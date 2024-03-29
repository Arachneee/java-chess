package controller.status;

public interface ChessProgramStatus {

    String readCommand();

    int getGameId();

    boolean isNotEnd();

    boolean isStarting();

    boolean isRunning();
}
