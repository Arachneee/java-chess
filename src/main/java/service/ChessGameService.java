package service;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.player.Player;
import domain.result.ChessGameResult;
import domain.square.Square;
import repository.ChessGameDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChessGameService {

    private final Connection connection;
    private final ChessGameDao chessGameDao;
    private final ChessBoardService chessBoardService;
    private final ChessResultService chessResultService;

    public ChessGameService(final Connection connection) {
        this.connection = connection;
        this.chessGameDao = new ChessGameDao(connection);
        this.chessBoardService = new ChessBoardService(connection);
        this.chessResultService = new ChessResultService(connection);
    }

    public ChessGame createNewGame(final Player blackPlayer, final Player whitePlayer) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final int gameId = chessGameDao.createGame(blackPlayer, whitePlayer, Team.WHITE, ChessGameStatus.RUNNING);
            final ChessBoard chessBoard = ChessBoard.create();
            chessBoardService.addBoard(chessBoard, gameId);

            connection.commit();
            return new ChessGame(gameId, blackPlayer, whitePlayer, chessBoard, ChessGameStatus.RUNNING, Team.WHITE);
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ChessGame findGameById(final int gameId) {
        return chessGameDao.findGameById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));
    }

    public List<Integer> findRunningGameIds() {
        final List<Integer> games = chessGameDao.findRunningGameIds();
        if (games.isEmpty()) {
            throw new IllegalArgumentException("진행중인 게임이 없습니다.");
        }
        return games;
    }

    public void move(final int gameId, final Square source, final Square target) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = findGameById(gameId);
            chessGame.move(source, target);

            chessGameDao.update(chessGame);
            chessBoardService.update(chessGame.getChessBoard(), gameId);

            if (chessGame.isEnd()) {
                chessResultService.saveResult(gameId);
            }

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void endGame(final int gameId) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = findGameById(gameId);
            chessGame.end();

            chessGameDao.update(chessGame);
            chessResultService.saveResult(gameId);

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean isRunningGame(final int gameId) {
        return chessGameDao.existRunningById(gameId);
    }

    public ChessGameResult calculateResult(final int gameId) {
        return chessResultService.calculateResult(gameId);
    }
}
