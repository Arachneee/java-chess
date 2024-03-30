package service;

import domain.chessboard.ChessBoard;
import repository.ChessBoardDao;

import java.sql.Connection;
import java.sql.SQLException;

public class ChessBoardService {

    private final Connection connection;
    private final ChessBoardDao chessBoardDao;

    public ChessBoardService(final Connection connection) {
        this.connection = connection;
        this.chessBoardDao = new ChessBoardDao(connection);
    }

    public ChessBoard findByGameId(final int gameId) {
        return chessBoardDao.findByGameId(gameId);
    }

    public void updateBoard(final ChessBoard chessBoard, final int gameId) throws SQLException {
        if (connection.getAutoCommit()) {
            try {
                connection.setAutoCommit(false);

                deleteAndCreate(chessBoard, gameId);

                connection.commit();
            } catch (final Exception e) {
                connection.rollback();
                throw new IllegalArgumentException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        }

        deleteAndCreate(chessBoard, gameId);
    }

    private void deleteAndCreate(final ChessBoard chessBoard, final int gameId) {
        chessBoardDao.deleteByGameId(gameId);
        chessBoardDao.addBoard(chessBoard, gameId);
    }
}
