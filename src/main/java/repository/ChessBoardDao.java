package repository;

import domain.chessboard.ChessBoard;
import domain.piece.Piece;
import domain.piece.PieceMaker;
import domain.square.Square;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardDao {

    private final Connection connection;

    public ChessBoardDao(final Connection connection) {
        this.connection = connection;
    }

    public void addBoard(final ChessBoard chessBoard, final int gameId) {
        chessBoard.getPieceSquares().forEach((square, piece) -> addSquarePiece(square, piece, gameId));
    }

    private void addSquarePiece(final Square square, final Piece piece, final int gameId) {
        final var query = "INSERT INTO board (file, `rank`, piece_type, team, game_id) VALUES (?, ?, ?, ?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, square.file().name());
            preparedStatement.setString(2, square.rank().name());
            preparedStatement.setString(3, piece.pieceType().name());
            preparedStatement.setString(4, piece.team().name());
            preparedStatement.setInt(5, gameId);

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChessBoard findChessBoard(final int gameId) {
        final var query = "SELECT * FROM board WHERE game_id = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);

            final var resultSet = preparedStatement.executeQuery();

            final Map<Square, Piece> squarePieces = new HashMap<>();

            while (resultSet.next()) {
                squarePieces.put(Square.of(
                                resultSet.getString("file"),
                                resultSet.getString("rank")
                        ),
                        PieceMaker.of(
                                resultSet.getString("piece_type"),
                                resultSet.getString("team")
                        ));
            }

            return new ChessBoard(squarePieces);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty(final int gameId) {
        final var query = "SELECT * FROM board WHERE game_id = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);

            final var resultSet = preparedStatement.executeQuery();

            return !resultSet.next();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByGameId(final int gameId) {
        final var query = "DELETE FROM board WHERE game_id = (?)";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameId);

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
