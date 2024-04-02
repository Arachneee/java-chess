package view.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public enum CommandFormat {
    START,
    CONTINUE,
    RECORD,
    RANKING,
    MOVE,
    STATUS,
    END,
    QUIT;

    private static final Pattern MOVE_FORMAT = Pattern.compile("^move [a-z]\\d [a-z]\\d$");
    private static final String COMMAND_DELIMITER = " ";
    private static final int COMMAND_KEY_INDEX = 0;
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    private final List<String> parameters = new ArrayList<>();

    public static CommandFormat from(final String input) {
        if (MOVE_FORMAT.matcher(input).matches()) {
            final List<String> commandInputs = Arrays.asList(input.split(COMMAND_DELIMITER));
            final CommandFormat command = MOVE;

            command.parameters.addAll(commandInputs.subList(SOURCE_INDEX, TARGET_INDEX));
            return command;
        }

        try {
            return valueOf(input.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }

    public List<String> getParameters() {
        return parameters;
    }
}
