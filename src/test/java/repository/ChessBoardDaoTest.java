package repository;

import connection.ChessConnectionGenerator;
import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
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

                gameId = chessGameDao.findAutoIncrement();
                final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                        .id(gameId)
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
        final ChessBoard chessBoard1 = chessBoardDao.findByGameId(gameId);

        // then
        assertThat(chessBoard1.getPieceSquares().entrySet()).isEqualTo(pieceSquares.entrySet());
    }

    @DisplayName("체스 보드를 지운다.")
    @Test
    void createAndDelete() {
        // given
        final ChessBoard chessBoard = ChessBoard.create();
        chessBoardDao.addBoard(chessBoard, gameId);

        // when
        chessBoardDao.deleteByGameId(gameId);

        // then
        assertThat(chessBoardDao.findByGameId(gameId).getPieceSquares()).isEmpty();
    }
}
