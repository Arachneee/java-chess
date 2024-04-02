package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.player.Player;
import domain.result.WinStatusSummary;
import service.ChessGameService;
import service.PlayerService;
import view.InputView;
import view.OutputView;

public class RecordCommand extends StartingCommand {

    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    public RecordCommand(final PlayerService playerService, final ChessGameService chessGameService) {
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus execute() {
        final Player player = readPlayer();
        final WinStatusSummary winStatusSummary = chessGameService.findGameRecord(player);
        OutputView.printGameRecord(winStatusSummary);

        return new StartingStatus();
    }

    private Player readPlayer() {
        while (true) {
            try {
                final String name = InputView.readPlayerName();
                return playerService.findPlayer(name);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
