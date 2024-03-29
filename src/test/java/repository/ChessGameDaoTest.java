package repository;

import connection.ChessConnectionGenerator;
import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.player.Player;
import domain.player.PlayerName;
import domain.square.Square;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ChessGameDaoTest {

    final Connection connection = ChessConnectionGenerator.getTestConnection();
    final ChessGameDao chessGameDao = new ChessGameDao(connection);
    final ChessBoardDao chessBoardDao = new ChessBoardDao(connection);
    final PlayerDao playerDao = new PlayerDao(connection);
    final PlayerName pobi = new PlayerName("pobi");
    final PlayerName json = new PlayerName("json");

    @BeforeEach
    void before() {
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                playerDao.add(pobi);
                playerDao.add(json);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void after() throws SQLException {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @DisplayName("게임을 추가하고 찾는다.")
    @Test
    void create() {
        // given
        final Player blackPlayer = new Player(pobi);
        final Player whitePlayer = new Player(json);
        final Team team = Team.WHITE;
        final ChessGameStatus status = ChessGameStatus.RUNNING;

        // when
        final int gameId = chessGameDao.createGame(blackPlayer, whitePlayer, team, status);

        // then
        final ChessGame chessGame = chessGameDao.findRunningGameById(gameId).get();

        assertAll(
                () -> assertThat(chessGame.getBlackPlayer()).isEqualTo(blackPlayer),
                () -> assertThat(chessGame.getWhitePlayer()).isEqualTo(whitePlayer),
                () -> assertThat(chessGame.getCurrentTeam()).isEqualTo(team),
                () -> assertThat(chessGame.getStatus()).isEqualTo(status)
        );
    }

    @DisplayName("게임의 상태를 업데이트 한다.")
    @Test
    void update() {
        // given
        final Player blackPlayer = new Player(pobi);
        final Player whitePlayer = new Player(json);
        final Team team = Team.WHITE;
        final ChessGameStatus status = ChessGameStatus.RUNNING;
        final int gameId = chessGameDao.createGame(blackPlayer, whitePlayer, team, status);
        chessBoardDao.addBoard(ChessBoard.create(), gameId);

        final ChessGame chessGame = chessGameDao.findRunningGameById(gameId).get();
        chessGame.move(Square.of("B", "TWO"), Square.of("B", "FOUR"));

        // when
        chessGameDao.updateChessGame(chessGame);

        // then
        final ChessGame findChessGame = chessGameDao.findRunningGameById(gameId).get();

        assertAll(
                () -> assertThat(findChessGame.getCurrentTeam()).isEqualTo(chessGame.getCurrentTeam()),
                () -> assertThat(findChessGame.getStatus()).isEqualTo(chessGame.getStatus())
        );
    }
}
