package dto;

import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.piece.Piece;
import domain.result.ChessResult;
import domain.square.Square;

import java.util.Map;

public record ChessGameDto(
        int gameNumber,
        String blackPlayerName,
        String whitePlayerName,
        String currentTeam,
        String currentPlayerName,
        Map<Square, Piece> board,
        ChessGameStatus status,
        ChessResult chessResult
) {

    public static ChessGameDto from(final ChessGame chessGame) {
        return new ChessGameDto(chessGame.getNumber(),
                chessGame.getBlackPlayer().getName(),
                chessGame.getWhitePlayer().getName(),
                chessGame.getCurrentTeam().name(),
                chessGame.getCurrentPlayer().getName(),
                chessGame.getChessBoard().getPieceSquares(),
                chessGame.getStatus(),
                chessGame.calculateResult());
    }
}
