package view.format;

public enum CommandFormat {
    START,
    CONTINUE,
    RECORD,
    RANKING,
    MOVE,
    STATUS,
    END,
    QUIT;

    public static CommandFormat from(final String input) {
        try {
            return valueOf(input.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }
}
