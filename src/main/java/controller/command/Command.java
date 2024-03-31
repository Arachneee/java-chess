package controller.command;

import controller.status.ChessProgramStatus;

import java.util.List;


public interface Command {

    ChessProgramStatus executeStarting();

    ChessProgramStatus executeRunning(List<String> inputs, int gameNumber);

    boolean isStarting();

    boolean isRunning();
}
