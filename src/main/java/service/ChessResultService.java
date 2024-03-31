package service;

import domain.WinStatus;
import domain.player.Player;
import domain.result.ChessResult;
import domain.result.WinStatusSummary;
import repository.ChessResultDao;

import java.sql.Connection;
import java.util.List;

public class ChessResultService {

    private final ChessResultDao chessResultDao;

    public ChessResultService(final Connection connection) {
        this.chessResultDao = new ChessResultDao(connection);
    }

    public WinStatusSummary findGameRecord(final Player player) {
        final List<WinStatus> blackWinStatuses = chessResultDao.findBlackWinStatus(player);
        final List<WinStatus> whiteWinStatuses = chessResultDao.findWhiteWinStatus(player);

        return WinStatusSummary.of(blackWinStatuses, whiteWinStatuses);
    }

    public void saveResult(final int gameNumber, final ChessResult chessResult) {
        chessResultDao.create(chessResult, gameNumber);
    }
}
