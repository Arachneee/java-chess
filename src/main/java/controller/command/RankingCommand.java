package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.StartingStatus;
import domain.player.Player;
import dto.PlayerRankingDto;
import service.ChessGameService;
import service.PlayerService;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class RankingCommand extends StartingCommand {

    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    public RankingCommand(final PlayerService playerService, final ChessGameService chessGameService) {
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus execute() throws SQLException {
        final List<Player> players = playerService.findAllPlayer();
        final List<PlayerRankingDto> rankings = chessGameService.getRanking(players);

        OutputView.printRanking(rankings);

        return new StartingStatus();
    }
}
