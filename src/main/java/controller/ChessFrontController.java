package controller;

import controller.command.*;
import controller.status.ChessProgramStatus;
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

        private static final int COMMAND_KEY_INDEX = 0;

        private final Map<String, Command> startingRouter;
        private final Map<String, Command> runningRouter;

        private CommandRouter(final Connection connection) {
            final PlayerService playerService = new PlayerService(connection);
            final ChessGameService chessGameService = new ChessGameService(connection);
            final ChessResultService chessResultService = new ChessResultService(connection);

            final QuitCommand quitCommand = new QuitCommand();

            this.startingRouter = Map.of(
                    "start", new StartCommand(playerService, chessGameService),
                    "continue", new ContinueCommand(chessGameService),
                    "record", new RecordCommand(playerService, chessResultService),
                    "quit", quitCommand);

            this.runningRouter = Map.of(
                    "move", new MoveCommand(chessGameService),
                    "status", new StatusCommand(chessGameService),
                    "end", new EndCommand(chessGameService),
                    "quit", quitCommand);
        }

        private ChessProgramStatus execute(final String command, final ChessProgramStatus status) throws SQLException {
            final List<String> commands = Arrays.asList(command.split(" "));
            final String commandKey = commands.get(COMMAND_KEY_INDEX);

            validateCommand(commandKey, status);

            if (status.isStarting()) {
                return startingRouter.get(commandKey).executeStart();
            }
            return runningRouter.get(commandKey).executePlay(commands, status.getGameId());
        }

        private void validateCommand(final String commandKey, final ChessProgramStatus status) {
            if (status.isStarting() && !startingRouter.containsKey(commandKey)) {
                throw new IllegalArgumentException("잘못된 커맨드입니다.");
            }
            if (status.isRunning() && !runningRouter.containsKey(commandKey)) {
                throw new IllegalArgumentException("잘못된 커맨드입니다.");
            }
            if (!status.isNotEnd()) {
                throw new IllegalStateException("프로그램이 종료 상태입니다.");
            }
        }
    }
}
