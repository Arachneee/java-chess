package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.player.Player;
import domain.result.WinStatusSummary;
import service.ChessResultService;
import service.PlayerService;
import view.InputView;
import view.OutputView;

public class RecordCommand extends StartingCommand {

    private final PlayerService playerService;
    private final ChessResultService chessResultService;

    public RecordCommand(final PlayerService playerService, final ChessResultService chessResultService) {
        this.playerService = playerService;
        this.chessResultService = chessResultService;
    }

    @Override
    public ChessProgramStatus executeStarting() {
        final Player player = readPlayer();
        final WinStatusSummary winStatusSummary = chessResultService.findGameRecord(player);
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
