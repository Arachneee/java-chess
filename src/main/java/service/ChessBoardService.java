package service;

import domain.chessboard.ChessBoard;
import domain.result.ChessGameResult;
import repository.ChessBoardDao;
import repository.ChessResultDao;

import java.sql.Connection;

public class ChessBoardService {

    private final ChessBoardDao chessBoardDao;
    private final ChessResultDao chessResultDao;

    public ChessBoardService(final Connection connection) {
        this.chessBoardDao = new ChessBoardDao(connection);
        this.chessResultDao = new ChessResultDao(connection);
    }

    public void addBoard(final ChessBoard chessBoard, final int gameId) {
        chessBoardDao.addBoard(chessBoard, gameId);
    }

    public void updateBoard(final ChessBoard chessBoard, final int gameId) {
        chessBoardDao.deleteByGameId(gameId);
        chessBoardDao.addBoard(chessBoard, gameId);
    }

    public void saveResult(final int gameId) {
        final ChessGameResult chessGameResult = calculateResult(gameId);
        chessResultDao.create(chessGameResult, gameId);
    }

    public ChessGameResult calculateResult(final int gameId) {
        final ChessBoard chessBoard = chessBoardDao.findChessBoard(gameId);

        return ChessGameResult.from(chessBoard.getPieceSquares());
    }
}
