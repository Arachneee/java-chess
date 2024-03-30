package service;

import domain.WinStatus;
import domain.chessboard.ChessBoard;
import domain.player.Player;
import domain.result.ChessGameResult;
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

    public void saveResult(final int gameId, final ChessBoard chessBoard) {
        final ChessGameResult chessGameResult = calculateResult(chessBoard);
        chessResultDao.create(chessGameResult, gameId);
    }

    public ChessGameResult calculateResult(final ChessBoard chessBoard) {
        return ChessGameResult.from(chessBoard.getPieceSquares());
    }
}
