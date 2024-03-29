package domain.game;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.player.Player;
import domain.square.Square;

public class ChessGame {

    private final int id;
    private final Player blackPlayer;
    private final Player whitePlayer;
    private final ChessBoard chessBoard;
    private ChessGameStatus status;
    private Team currentTeam;

    public ChessGame(
            final int id,
            final Player blackPlayer,
            final Player whitePlayer,
            final ChessBoard chessBoard,
            final ChessGameStatus status,
            final Team currentTeam
    ) {
        this.id = id;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.chessBoard = chessBoard;
        this.status = status;
        this.currentTeam = currentTeam;
    }

    public void move(final Square source, final Square target) {
        chessBoard.move(source, target, currentTeam);

        currentTeam = currentTeam.turn();

        if (chessBoard.isKingDead()) {
            end();
        }
    }

    public void end() {
        status = ChessGameStatus.END;
    }

    public boolean isEnd() {
        return status == ChessGameStatus.END;
    }

    public Player getCurrentPlayer() {
        if (currentTeam == Team.WHITE) {
            return whitePlayer;
        }
        return blackPlayer;
    }

    public int getId() {
        return id;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public ChessGameStatus getStatus() {
        return status;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public static class ChessGameBuilder {
        int id;
        Player blackPlayer;
        Player whitePlayer;
        ChessBoard chessBoard;
        ChessGameStatus status;
        Team currentTeam;

        public static ChessGameBuilder builder() {
            return new ChessGameBuilder();
        }

        public ChessGameBuilder id(final int id) {
            this.id = id;
            return this;
        }

        public ChessGameBuilder blackPlayer(final Player blackPlayer) {
            this.blackPlayer = blackPlayer;
            return this;
        }

        public ChessGameBuilder whitePlayer(final Player whitePlayer) {
            this.whitePlayer = whitePlayer;
            return this;
        }

        public ChessGameBuilder chessBoard(final ChessBoard chessBoard) {
            this.chessBoard = chessBoard;
            return this;
        }

        public ChessGameBuilder status(final ChessGameStatus status) {
            this.status = status;
            return this;
        }

        public ChessGameBuilder currentTeam(final Team currentTeam) {
            this.currentTeam = currentTeam;
            return this;
        }

        public ChessGame build() {
            return new ChessGame(id, blackPlayer, whitePlayer, chessBoard, status, currentTeam);
        }
    }
}
