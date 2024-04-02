package controller.status;

import view.format.CommandFormat;

public interface ChessControllerStatus {

    CommandFormat readCommand();

    ChessControllerStatus execute(final CommandFormat commandFormat) throws Exception;
}
