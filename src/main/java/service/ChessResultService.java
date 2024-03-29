package service;

import domain.WinStatus;
import domain.chessboard.ChessBoard;
import domain.player.Player;
import domain.result.ChessGameResult;
import domain.result.WinStatusSummary;
import repository.ChessBoardDao;
import repository.ChessResultDao;

import java.sql.Connection;
import java.util.List;

public class ChessResultService {

    private final ChessResultDao chessResultDao;
    private final ChessBoardDao chessBoardDao;

    public ChessResultService(final Connection connection) {
        this.chessResultDao = new ChessResultDao(connection);
        this.chessBoardDao = new ChessBoardDao(connection);
    }

    public void saveResult(final int gameId) {
        final ChessGameResult chessGameResult = calculateResult(gameId);
        chessResultDao.create(chessGameResult, gameId);
    }

    public ChessGameResult calculateResult(final int gameId) {
        final ChessBoard chessBoard = chessBoardDao.findChessBoard(gameId);

        return ChessGameResult.from(chessBoard.getPieceSquares());
    }

    public WinStatusSummary findGameRecord(final Player player) {
        final List<WinStatus> blackWinStatuses = chessResultDao.findBlackWinStatus(player);
        final List<WinStatus> whiteWinStatuses = chessResultDao.findWhiteWinStatus(player);

        return WinStatusSummary.of(blackWinStatuses, whiteWinStatuses);
    }
}
