package controller;

import controller.command.*;
import controller.status.ChessProgramStatus;
import controller.status.EndStatus;
import controller.status.StartingStatus;
import service.ChessGameService;
import service.PlayerService;
import view.OutputView;
import view.format.CommandFormat;

import java.sql.Connection;
import java.sql.SQLException;
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
                final CommandFormat command = status.readCommand();
                status = commandRouter.execute(command, status);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private static class CommandRouter {

        private final Map<CommandFormat, RunningCommand> runningCommandRouter;
        private final Map<CommandFormat, StartingCommand> startingCommandRouter;

        private CommandRouter(final Connection connection) {
            final PlayerService playerService = new PlayerService(connection);
            final ChessGameService chessGameService = new ChessGameService(connection);

            this.startingCommandRouter = Map.of(
                    CommandFormat.START, new NewGameCommand(playerService, chessGameService),
                    CommandFormat.CONTINUE, new ContinueGameCommand(chessGameService),
                    CommandFormat.RECORD, new RecordCommand(playerService, chessGameService),
                    CommandFormat.RANKING, new RankingCommand(playerService, chessGameService)
            );

            this.runningCommandRouter = Map.of(
                    CommandFormat.MOVE, new MoveCommand(chessGameService),
                    CommandFormat.STATUS, new StatusCommand(chessGameService),
                    CommandFormat.END, new EndCommand(chessGameService)
            );
        }

        private ChessProgramStatus execute(final CommandFormat commandFormat, final ChessProgramStatus status) throws SQLException {
            if (CommandFormat.QUIT == commandFormat) {
                return new EndStatus();
            }

            if (status.isStarting() && startingCommandRouter.containsKey(commandFormat)) {
                final StartingCommand startingCommand = startingCommandRouter.get(commandFormat);
                return startingCommand.execute();
            }
            if (status.isRunning() && runningCommandRouter.containsKey(commandFormat)) {
                final RunningCommand runningCommand = runningCommandRouter.get(commandFormat);
                return runningCommand.execute(commandFormat.getParameters(), status.getGameNumber());
            }

            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }
}
