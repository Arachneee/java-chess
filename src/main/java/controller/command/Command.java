package controller.command;

import controller.status.ChessProgramStatus;

import java.sql.SQLException;
import java.util.List;


public interface Command {

    ChessProgramStatus executeStarting() throws SQLException;

    ChessProgramStatus executeRunning(List<String> inputs, int gameNumber) throws SQLException;

    boolean isStarting();

    boolean isRunning();
}
