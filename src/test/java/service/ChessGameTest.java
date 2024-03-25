package service;

import domain.square.File;
import domain.square.Rank;
import domain.square.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessGameTest {

    @DisplayName("King이 잡히면 게임이 종료된다.")
    @Test
    void kingDead() {
        // given
        final ChessGame chessGame = new ChessGame();
        chessGame.start();

        chessGame.move(new Square(File.F, Rank.TWO), new Square(File.F, Rank.THREE));
        chessGame.move(new Square(File.E, Rank.SEVEN), new Square(File.E, Rank.FIVE));
        /*
        RNBQKBNR  8 (rank 8)
        PPPP.PPP  7
        ........  6
        ....P...  5
        ........  4
        .....p..  3
        ppppp.pp  2
        rnbqkbnr  1 (rank 1)

        abcdefgh
         */
        chessGame.move(new Square(File.G, Rank.TWO), new Square(File.G, Rank.FOUR));
        chessGame.move(new Square(File.D, Rank.EIGHT), new Square(File.H, Rank.FOUR));
        chessGame.move(new Square(File.B, Rank.TWO), new Square(File.B, Rank.THREE));
        /*
        RNB.KBNR  8 (rank 8)
        PPPP.PPP  7
        ........  6
        ....P...  5
        ......pQ  4
        .p...p..  3
        p.ppp..p  2
        rnbqkbnr  1 (rank 1)

        abcdefgh
         */

        // when
        chessGame.move(new Square(File.H, Rank.FOUR), new Square(File.E, Rank.ONE));
        /*
        RNB.KBNR  8 (rank 8)
        PPPP.PPP  7
        ........  6
        ....P...  5
        ......p.  4
        .p...p..  3
        p.ppp..p  2
        rnbqQbnr  1 (rank 1)

        abcdefgh
         */

        // then
        assertThat(chessGame.isNotEnd()).isFalse();
    }
}