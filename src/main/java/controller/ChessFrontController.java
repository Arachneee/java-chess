package controller;

import controller.command.*;
import controller.status.ChessProgramStatus;
import controller.status.EndStatus;
import controller.status.StartingStatus;
import service.ChessGameService;
import service.ChessResultService;
import service.PlayerService;
import view.OutputView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ChessFrontController {

    private final CommandRouter commandRouter;
    private ChessProgramStatus status;

    public ChessFrontController(final Connection connection) {
        this.commandRouter = new CommandRouter(connection);
        this.status = new StartingStatus();
    }

    public void run() {
        while (status.isNotEnd()) {
            try {
                final String command = status.readCommand();
                status = commandRouter.execute(command, status);
            } catch (final IllegalArgumentException | SQLException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private static class CommandRouter {

        private static final Pattern MOVE_FORMAT = Pattern.compile("^move [a-z]\\d [a-z]\\d$");
        private static final String COMMAND_DELIMITER = " ";
        private static final int COMMAND_KEY_INDEX = 0;

        private final Map<String, Command> router;

        private CommandRouter(final Connection connection) {
            final PlayerService playerService = new PlayerService(connection);
            final ChessGameService chessGameService = new ChessGameService(connection);
            final ChessResultService chessResultService = new ChessResultService(connection);

            this.router = Map.of(
                    "start", new NewGameCommand(playerService, chessGameService),
                    "continue", new ContinueGameCommand(chessGameService),
                    "record", new RecordCommand(playerService, chessResultService),
                    "move", new MoveCommand(chessGameService),
                    "status", new StatusCommand(chessGameService),
                    "end", new EndCommand(chessGameService));
        }

        private ChessProgramStatus execute(final String commandInput, final ChessProgramStatus status) throws SQLException {
            if ("quit".equals(commandInput)) {
                return new EndStatus();
            }
            validateCommandInput(commandInput);

            final List<String> commandInputs = Arrays.asList(commandInput.split(COMMAND_DELIMITER));
            final String commandKey = commandInputs.get(COMMAND_KEY_INDEX);

            final Command command = router.get(commandKey);
            if (status.isStarting() && command.isStarting()) {
                return command.executeStarting();
            }
            if (status.isRunning() && command.isRunning()) {
                return command.executeRunning(commandInputs, status.getGameNumber());
            }

            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }

        private void validateCommandInput(final String commandInput) {
            if (router.containsKey(commandInput) || MOVE_FORMAT.matcher(commandInput).matches()) {
                return;
            }
            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }
}
