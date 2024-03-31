package service;

import domain.WinStatus;
import domain.piece.Piece;
import domain.player.Player;
import domain.result.ChessResult;
import domain.result.WinStatusSummary;
import domain.square.Square;
import repository.ChessResultDao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

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

    public ChessResult calculateChessResult(final Map<Square, Piece> squarePieces) {
        return ChessResult.from(squarePieces);
    }
}
