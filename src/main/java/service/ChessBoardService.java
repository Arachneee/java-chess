package service;

import domain.ChessGameStatus;
import domain.Team;
import domain.chessboard.ChessBoard;
import domain.piece.Piece;
import domain.square.Square;
import repository.ChessBoardDao;
import repository.ChessGameDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ChessBoardService {

    private final Connection connection;
    private final ChessBoardDao chessBoardDao;
    private final ChessGameDao chessGameDao;


    public ChessBoardService(final Connection connection) {
        this.connection = connection;
        this.chessBoardDao = new ChessBoardDao(connection);
        this.chessGameDao = new ChessGameDao(connection);
    }

    public void move(final int gameId, final Square source, final Square target) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessBoard chessBoard = createChessBoard(gameId);
            chessBoard.move(source, target);

            chessBoardDao.deleteByGameId(gameId);
            chessBoardDao.addBoard(chessBoard, gameId);
            chessGameDao.updateCurrentTeam(gameId, chessBoard.currentTeam());

            if (isKingDead(gameId)) {
                endGame(gameId);
            }
            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private ChessBoard createChessBoard(final int gameId) {
        final Map<Square, Piece> pieceSquares = chessBoardDao.findAll(gameId);
        final Team currentTeam = chessGameDao.findCurrentTeamById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임이 없습니다."));

        return new ChessBoard(pieceSquares, currentTeam);
    }

    public void endGame(final int gameId) {
        chessGameDao.updateStatusById(gameId, ChessGameStatus.END);
    }

    public boolean isKingDead(final int gameId) {
        return chessBoardDao.countKing(gameId) < 2;
    }

    public Map<Square, Piece> getPieceSquares(final int gameId) {
        return chessBoardDao.findAll(gameId);
    }
}
