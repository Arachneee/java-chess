package domain.piece;

import domain.ChessVector;
import domain.Team;
import domain.square.Rank;
import domain.square.Square;

import java.util.List;
import java.util.Objects;

public class Pawn extends Piece {

    private static final List<ChessVector> BLACK_ATTACK_VECTORS = List.of(new ChessVector(1, -1),
            new ChessVector(-1, -1));
    private static final List<ChessVector> WHITE_ATTACK_VECTORS = List.of(new ChessVector(-1, 1),
            new ChessVector(1, 1));
    private static final ChessVector BLACK_START_VECTOR = new ChessVector(0, -2);
    private static final ChessVector WHITE_START_VECTOR = new ChessVector(0, 2);
    private static final ChessVector BLACK_MOVE_VECTOR = new ChessVector(0, -1);
    private static final ChessVector WHITE_MOVE_VECTOR = new ChessVector(0, 1);
    private static final Rank BLACK_START_RANK = Rank.SEVEN;
    private static final Rank WHITE_START_RANK = Rank.TWO;

    public Pawn(final Team team) {
        super(team);
    }

    @Override
    public boolean canMove(final Square source, final Square target) {
        if (team == Team.BLACK) {
            return canBlackMove(source, target);
        }
        return canWhiteMove(source, target);
    }

    @Override
    public boolean canAttack(final Square source, final Square target) {
        final ChessVector chessVector = target.calculateVector(source);

        if (team == Team.BLACK) {
            return BLACK_ATTACK_VECTORS.contains(chessVector);
        }
        return WHITE_ATTACK_VECTORS.contains(chessVector);
    }

    private boolean canBlackMove(final Square source, final Square target) {
        final ChessVector chessVector = target.calculateVector(source);

        return (source.isRank(BLACK_START_RANK) && BLACK_START_VECTOR.equals(chessVector))
                || chessVector.equals(BLACK_MOVE_VECTOR);
    }

    private boolean canWhiteMove(final Square source, final Square target) {
        final ChessVector chessVector = target.calculateVector(source);

        return (source.isRank(WHITE_START_RANK) && WHITE_START_VECTOR.equals(chessVector))
                || chessVector.equals(WHITE_MOVE_VECTOR);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Pawn piece)) {
            return false;
        }
        return this.team == piece.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, Pawn.class);
    }
}
