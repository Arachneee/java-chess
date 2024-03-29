package repository;

import connection.ChessConnectionGenerator;
import domain.ChessGameStatus;
import domain.Team;
import domain.chessboard.ChessBoard;
import domain.piece.Piece;
import domain.player.Player;
import domain.player.PlayerName;
import domain.square.Square;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ChessBoardDaoTest {
    final Connection connection = ChessConnectionGenerator.getTestConnection();
    final PlayerDao playerDao = new PlayerDao(connection);
    final ChessGameDao chessGameDao = new ChessGameDao(connection);

    final ChessBoardDao chessBoardDao = new ChessBoardDao(connection);

    int gameId;
    final PlayerName pobi = new PlayerName("pobi");
    final PlayerName json = new PlayerName("json");

    @BeforeEach
    void before() {
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
                playerDao.add(pobi);
                playerDao.add(json);

                gameId = chessGameDao.addGame(new Player(pobi), new Player(json),
                        Team.WHITE, ChessGameStatus.RUNNING);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void after() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("전체 위치 별 기물을 찾는다.")
    @Test
    void createAndFind() {
        // given
        final ChessBoard chessBoard = ChessBoard.create();
        final Map<Square, Piece> pieceSquares = chessBoard.getPieceSquares();
        chessBoardDao.addBoard(chessBoard, gameId);

        // when
        final Map<Square, Piece> results = chessBoardDao.findAll(gameId);

        // then
        assertThat(results.entrySet()).isEqualTo(pieceSquares.entrySet());
    }
}
