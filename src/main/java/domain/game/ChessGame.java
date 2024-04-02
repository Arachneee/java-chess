package domain.game;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.piece.Piece;
import domain.player.Player;
import domain.result.ChessResult;
import domain.square.Square;

public class ChessGame {

    private final int number;
    private final Player blackPlayer;
    private final Player whitePlayer;
    private final ChessBoard chessBoard;
    private ChessGameStatus status;
    private Team currentTeam;

    public ChessGame(
            final int number,
            final Player blackPlayer,
            final Player whitePlayer,
            final ChessBoard chessBoard,
            final ChessGameStatus status,
            final Team currentTeam
    ) {
        this.number = number;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.chessBoard = chessBoard;
        this.status = status;
        this.currentTeam = currentTeam;
    }

    public void move(final Square source, final Square target) {
        validateTeam(source);

        chessBoard.move(source, target);

        currentTeam = currentTeam.turn();

        if (chessBoard.isKingDead()) {
            end();
        }
    }

    private void validateTeam(final Square source) {
        final Piece piece = chessBoard.findPiece(source);

        if (piece.isOppositeTeam(currentTeam)) {
            throw new IllegalArgumentException("상대방의 말을 움직일 수 없습니다.");
        }
    }

    public ChessResult calculateResult() {
        return ChessResult.from(chessBoard.getPieceSquares());
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

    public int getNumber() {
        return number;
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
        int number;
        Player blackPlayer;
        Player whitePlayer;
        ChessBoard chessBoard;
        ChessGameStatus status;
        Team currentTeam;

        public static ChessGameBuilder builder() {
            return new ChessGameBuilder();
        }

        public ChessGameBuilder number(final int number) {
            this.number = number;
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
            return new ChessGame(number, blackPlayer, whitePlayer, chessBoard, status, currentTeam);
        }
    }
}
