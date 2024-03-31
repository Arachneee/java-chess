package dto;

import domain.Team;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.piece.Piece;
import domain.square.Square;

import java.util.List;
import java.util.Map;

public record ChessGameDto(
        int gameNumber,
        String blackPlayerName,
        String whitePlayerName,
        String currentTeam,
        String currentPlayerName,
        Map<Square, Piece> board,
        ChessGameStatus status
) {

    public static ChessGameDto from(final ChessGame chessGame) {
        return new ChessGameDto(chessGame.getNumber(),
                chessGame.getBlackPlayer().getName(),
                chessGame.getWhitePlayer().getName(),
                chessGame.getCurrentTeam().name(),
                chessGame.getCurrentPlayer().getName(),
                chessGame.getChessBoard().getPieceSquares(),
                chessGame.getStatus());
    }
}
