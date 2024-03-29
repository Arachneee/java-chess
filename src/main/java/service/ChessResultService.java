package service;

import domain.WinStatus;
import domain.piece.Piece;
import domain.player.Player;
import domain.result.ChessGameResult;
import domain.result.WinStatusSummary;
import domain.square.Square;
import repository.ChessBoardDao;
import repository.ChessResultDao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

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
        final Map<Square, Piece> pieceSquares = chessBoardDao.findAll(gameId);

        return ChessGameResult.from(pieceSquares);
    }

    public WinStatusSummary findGameRecord(final Player player) {
        final List<WinStatus> blackWinStatuses = chessResultDao.findBlackWinStatus(player);
        final List<WinStatus> whiteWinStatuses = chessResultDao.findWhiteWinStatus(player);

        return WinStatusSummary.of(blackWinStatuses, whiteWinStatuses);
    }
}
