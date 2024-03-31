package repository;

import connection.TestChessConnectionGenerator;
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

    final Connection connection = TestChessConnectionGenerator.getConnection();
    final ChessGameDao chessGameDao = new ChessGameDao(connection);
    final ChessBoardDao chessBoardDao = new ChessBoardDao(connection);
    final PlayerDao playerDao = new PlayerDao(connection);
    final PlayerName pobi = new PlayerName("pobi");
    final PlayerName json = new PlayerName("json");
    int gameNumber;

    ChessGameDaoTest() throws SQLException {
    }

    @BeforeEach
    void before() {
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                playerDao.add(pobi);
                playerDao.add(json);

                gameNumber = chessGameDao.findMaxNumber();
                final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                        .number(gameNumber)
                        .blackPlayer(new Player(pobi))
                        .whitePlayer(new Player(json))
                        .chessBoard(ChessBoard.create())
                        .status(ChessGameStatus.RUNNING)
                        .currentTeam(Team.WHITE)
                        .build();

                chessGameDao.create(chessGame);
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
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }

    @DisplayName("게임의 상태를 업데이트 한다.")
    @Test
    void update() {
        // given
        final ChessGame chessGame = chessGameDao.findByNumberAndStatus(gameNumber, ChessGameStatus.RUNNING).get();
        chessGame.move(Square.of("B", "TWO"), Square.of("B", "FOUR"));

        // when
        chessGameDao.update(chessGame);

        // then
        final ChessGame findChessGame = chessGameDao.findByNumberAndStatus(gameNumber, ChessGameStatus.RUNNING).get();

        assertAll(
                () -> assertThat(findChessGame.getCurrentTeam()).isEqualTo(chessGame.getCurrentTeam()),
                () -> assertThat(findChessGame.getStatus()).isEqualTo(chessGame.getStatus())
        );
    }
}
