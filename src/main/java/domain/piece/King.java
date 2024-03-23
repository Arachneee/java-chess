package domain.piece;

import domain.ChessVector;
import domain.Team;
import domain.square.Square;

import java.util.Objects;

public class King extends Piece {

    private static final int MOVE_DISTANCE = 1;

    public King(final Team team) {
        super(team);
    }

    @Override
    public boolean canMove(final Square source, final Square target) {
        final ChessVector chessVector = target.calculateVector(source);

        return chessVector.isManhattanDistance(MOVE_DISTANCE);
    }

    @Override
    protected boolean canAttack(final Square source, final Square target) {
        return canNotMove(source, target);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final King piece)) {
            return false;
        }
        return this.team == piece.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, King.class);
    }
}
