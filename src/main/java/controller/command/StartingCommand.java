package controller.command;

import controller.status.ChessProgramStatus;

import java.sql.SQLException;

public abstract class StartingCommand {

    public abstract ChessProgramStatus execute() throws SQLException;
}
