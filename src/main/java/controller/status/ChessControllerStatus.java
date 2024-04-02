package controller.status;

import view.format.Command;

import java.sql.SQLException;

public interface ChessControllerStatus {

    Command readCommand();

    ChessControllerStatus execute(final Command commandFormat) throws SQLException;
}
