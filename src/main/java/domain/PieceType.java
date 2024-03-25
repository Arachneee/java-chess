package domain;

public enum PieceType {
    PAWN(1.0),
    KNIGHT(2.5),
    ROOK(5),
    BISHOP(3),
    QUEEN(9),
    KING(0);

    private final Score score;

    PieceType(final double value) {
        this.score = new Score(value);
    }

    // TODO : getter 제거
    public Score getScore() {
        return score;
    }
}