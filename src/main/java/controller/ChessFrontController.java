package controller;

import controller.command.*;
import controller.status.ChessProgramStatus;
import controller.status.EndStatus;
import controller.status.StartingStatus;
import service.ChessGameService;
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

    public void run() throws SQLException {
        while (status.isNotEnd()) {
            try {
                final String command = status.readCommand();
                status = commandRouter.execute(command, status);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private static class CommandRouter {

        private static final Pattern MOVE_FORMAT = Pattern.compile("^move [a-z]\\d [a-z]\\d$");
        private static final String COMMAND_DELIMITER = " ";
        private static final int COMMAND_KEY_INDEX = 0;

        private final Map<String, RunningCommand> runningCommandRouter;
        private final Map<String, StartingCommand> startingCommandRouter;

        private CommandRouter(final Connection connection) {
            final PlayerService playerService = new PlayerService(connection);
            final ChessGameService chessGameService = new ChessGameService(connection);

            this.startingCommandRouter = Map.of(
                    "start", new NewGameCommand(playerService, chessGameService),
                    "continue", new ContinueGameCommand(chessGameService),
                    "record", new RecordCommand(playerService, chessGameService),
                    "ranking", new RankingCommand(playerService, chessGameService)
            );

            this.runningCommandRouter = Map.of(
                    "move", new MoveCommand(chessGameService),
                    "status", new StatusCommand(chessGameService),
                    "end", new EndCommand(chessGameService)
            );
        }

        private ChessProgramStatus execute(final String commandInput, final ChessProgramStatus status) throws SQLException {
            if ("quit".equals(commandInput)) {
                return new EndStatus();
            }

            final List<String> commandInputs = Arrays.asList(commandInput.split(COMMAND_DELIMITER));
            final String commandKey = commandInputs.get(COMMAND_KEY_INDEX);

            if (status.isStarting() && startingCommandRouter.containsKey(commandKey)) {
                final StartingCommand startingCommand = startingCommandRouter.get(commandKey);
                return startingCommand.execute();
            }
            if (status.isRunning()
                    && (runningCommandRouter.containsKey(commandKey) || MOVE_FORMAT.matcher(commandInput).matches())) {
                final RunningCommand runningCommand = runningCommandRouter.get(commandKey);
                return runningCommand.execute(commandInputs, status.getGameNumber());
            }

            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }
}
