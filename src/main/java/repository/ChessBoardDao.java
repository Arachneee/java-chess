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

    public void addBoard(final ChessBoard chessBoard, final int gameNumber) {
        final var query = "INSERT INTO board (file, `rank`, piece_type, team, game_id) VALUES (?, ?, ?, ?, " +
                "(SELECT id FROM game WHERE number = (?)))";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            for (final var entry : chessBoard.getPieceSquares().entrySet()) {
                final var square = entry.getKey();
                final var piece = entry.getValue();

                preparedStatement.setString(1, square.file().name());
                preparedStatement.setString(2, square.rank().name());
                preparedStatement.setString(3, piece.pieceType().name());
                preparedStatement.setString(4, piece.team().name());
                preparedStatement.setInt(5, gameNumber);

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChessBoard findByGameNumber(final int gameNumber) {
        final var query = "SELECT * FROM board " +
                "WHERE game_id = (SELECT id FROM game WHERE number = (?))";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameNumber);

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

    public void deleteByGameNumber(final int gameNumber) {
        final var query = "DELETE FROM board " +
                "WHERE game_id = (SELECT id FROM game WHERE number = (?))";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, gameNumber);

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
