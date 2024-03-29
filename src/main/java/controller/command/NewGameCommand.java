package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.Team;
import domain.game.ChessGame;
import domain.player.Player;
import service.ChessGameService;
import service.PlayerService;
import view.InputView;
import view.OutputView;

import java.sql.SQLException;

public class NewGameCommand extends StartingCommand {

    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    public NewGameCommand(final PlayerService playerService, final ChessGameService chessGameService) {
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStarting() throws SQLException {
        final Player blackPlayer = roadPlayer(Team.BLACK);
        final Player whitePlayer = roadPlayer(Team.WHITE);
        final ChessGame chessGame = chessGameService.createNewGame(blackPlayer, whitePlayer);

        printStartGame(chessGame);

        return new RunningStatus(chessGame);
    }

    private Player roadPlayer(final Team team) {
        while (true) {
            try {
                final String name = InputView.readTeamPlayerName(team);
                return playerService.roadPlayer(name);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
