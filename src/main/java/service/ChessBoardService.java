package service;

import domain.chessboard.ChessBoard;
import repository.ChessBoardDao;

import java.sql.Connection;

public class ChessBoardService {

    private final ChessBoardDao chessBoardDao;

    public ChessBoardService(final Connection connection) {
        this.chessBoardDao = new ChessBoardDao(connection);
    }

    public void addBoard(final ChessBoard chessBoard, final int gameId) {
        chessBoardDao.addBoard(chessBoard, gameId);
    }

    public void update(final ChessBoard chessBoard, final int gameId) {
        chessBoardDao.deleteByGameId(gameId);
        chessBoardDao.addBoard(chessBoard, gameId);
    }
}
