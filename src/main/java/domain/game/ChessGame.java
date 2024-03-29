package domain.game;

import domain.Team;
import domain.chessboard.ChessBoard;
import domain.piece.Piece;
import domain.player.Player;
import domain.result.ChessGameResult;
import domain.square.Square;

import java.util.Map;

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

    public ChessGameResult calculateResult() {
        final Map<Square, Piece> pieceSquares = chessBoard.getPieceSquares();
        return ChessGameResult.from(pieceSquares);
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
}
