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

            final int gameId = chessGameDao.findAutoIncrement();
            final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                    .id(gameId)
                    .blackPlayer(blackPlayer)
                    .whitePlayer(whitePlayer)
                    .chessBoard(ChessBoard.create())
                    .status(ChessGameStatus.RUNNING)
                    .currentTeam(Team.WHITE)
                    .build();

            chessGameDao.create(chessGame);

            connection.commit();
            return chessGame;
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ChessGame findGameById(final int gameId) {
        return chessGameDao.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));
    }

    public ChessGame findRunningGameById(final int gameId) {
        return chessGameDao.findByIdAndStatus(gameId, ChessGameStatus.RUNNING)
                .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임을 찾을 수 없습니다."));
    }

    public List<Integer> findRunningGameIds() {
        final List<Integer> games = chessGameDao.findIdsByStatus(ChessGameStatus.RUNNING);
        if (games.isEmpty()) {
            throw new IllegalArgumentException("진행중인 게임이 없습니다.");
        }
        return games;
    }

    public void move(final int gameId, final Square source, final Square target) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = findRunningGameById(gameId);
            chessGame.move(source, target);

            chessGameDao.update(chessGame);
            chessBoardService.updateBoard(chessGame.getChessBoard(), gameId);

            if (chessGame.isEnd()) {
                chessResultService.saveResult(gameId, chessGame.getChessBoard());
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

            final ChessGame chessGame = findRunningGameById(gameId);
            chessGame.end();

            chessGameDao.update(chessGame);
            chessResultService.saveResult(gameId, chessGame.getChessBoard());

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ChessGameResult calculateResult(final int gameId) {
        final ChessBoard chessBoard = chessBoardService.findByGameId(gameId);
        return chessResultService.calculateResult(chessBoard);
    }
}
