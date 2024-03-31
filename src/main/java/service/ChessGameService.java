package service;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.player.Player;
import domain.square.Square;
import repository.ChessGameDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChessGameService {

    private final Connection connection;
    private final ChessGameDao chessGameDao;
    private final ChessResultService chessResultService;

    public ChessGameService(final Connection connection) {
        this.connection = connection;
        this.chessGameDao = new ChessGameDao(connection);
        this.chessResultService = new ChessResultService(connection);
    }

    public ChessGame createNewGame(final Player blackPlayer, final Player whitePlayer) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final int gameNumber = chessGameDao.findMaxNumber() + 1;
            final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                    .number(gameNumber)
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
            throw new RuntimeException("서버 오류입니다.");
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ChessGame findGameByNumber(final int gameNumber) {
        return chessGameDao.findByNumber(gameNumber)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));
    }

    public ChessGame findRunningGameByNumber(final int gameNumber) {
        return chessGameDao.findByNumberAndStatus(gameNumber, ChessGameStatus.RUNNING)
                .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임을 찾을 수 없습니다."));
    }

    public List<Integer> findRunningGameNumbers() {
        final List<Integer> games = chessGameDao.findNumbersByStatus(ChessGameStatus.RUNNING);
        if (games.isEmpty()) {
            throw new IllegalArgumentException("진행중인 게임이 없습니다.");
        }
        return games;
    }

    public void move(final int gameNumber, final Square source, final Square target) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = findRunningGameByNumber(gameNumber);
            chessGame.move(source, target);

            chessGameDao.update(chessGame);

            if (chessGame.isEnd()) {
                chessResultService.saveResult(gameNumber, chessGame.getChessResult());
            }

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void endGame(final int gameNumber) throws SQLException {
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = findRunningGameByNumber(gameNumber);
            chessGame.end();

            chessGameDao.updateStatus(chessGame);
            chessResultService.saveResult(gameNumber, chessGame.getChessResult());

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
