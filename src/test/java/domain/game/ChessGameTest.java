package domain.game;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.player.Player;
import domain.player.PlayerName;
import domain.square.File;
import domain.square.Rank;
import domain.square.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChessGameTest {

    @DisplayName("현재 턴인 팀의 기물만 움직일 수 있다.")
    @Test
    void validateSameTeam() {
        final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                .number(1)
                .blackPlayer(new Player(new PlayerName("pobi")))
                .whitePlayer(new Player(new PlayerName("json")))
                .chessBoard(ChessBoard.create())
                .status(ChessGameStatus.RUNNING)
                .currentTeam(Team.WHITE)
                .build();

        final Square source = new Square(File.A, Rank.SEVEN);
        final Square target = new Square(File.A, Rank.FIVE);

        assertThatThrownBy(() -> chessGame.move(source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상대방의 말을 움직일 수 없습니다.");
    }

    @DisplayName("White팀이 처음 시작하고 기물을 움직이면 현재 팀이 변경된다.")
    @Test
    void turn() {
        // given
        final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                .number(1)
                .blackPlayer(new Player(new PlayerName("pobi")))
                .whitePlayer(new Player(new PlayerName("json")))
                .chessBoard(ChessBoard.create())
                .status(ChessGameStatus.RUNNING)
                .currentTeam(Team.WHITE)
                .build();

        final Square source = new Square(File.B, Rank.TWO);
        final Square target = new Square(File.B, Rank.FOUR);

        // when
        chessGame.move(source, target);

        // then
        assertThat(chessGame.getCurrentTeam()).isEqualTo(Team.BLACK);
    }

    @DisplayName("King 죽으면 게임이 종료된다.")
    @Test
    void kingDead() {
        // given
        final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                .number(1)
                .blackPlayer(new Player(new PlayerName("pobi")))
                .whitePlayer(new Player(new PlayerName("json")))
                .chessBoard(ChessBoard.create())
                .status(ChessGameStatus.RUNNING)
                .currentTeam(Team.WHITE)
                .build();

        final Square source = new Square(File.B, Rank.TWO);
        final Square target = new Square(File.B, Rank.FOUR);

        // when
        chessGame.move(Square.from("e2"), Square.from("e3"));
        chessGame.move(Square.from("f7"), Square.from("f6"));
        chessGame.move(Square.from("d1"), Square.from("h5"));
        chessGame.move(Square.from("g8"), Square.from("h6"));
        chessGame.move(Square.from("h5"), Square.from("e8"));

        // then
        assertThat(chessGame.getStatus()).isEqualTo(ChessGameStatus.END);
    }
}
