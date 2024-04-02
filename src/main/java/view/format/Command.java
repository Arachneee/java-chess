package view.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Command {

    private static final Pattern MOVE_FORMAT = Pattern.compile("^move [a-z]\\d [a-z]\\d$");
    private static final String COMMAND_DELIMITER = " ";
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 3;

    private final CommandFormat commandFormat;
    private final List<String> parameters;

    private Command(final CommandFormat commandFormat) {
        this.commandFormat = commandFormat;
        this.parameters = new ArrayList<>();
    }

    private Command(final CommandFormat commandFormat, final List<String> parameters) {
        this.commandFormat = commandFormat;
        this.parameters = parameters;
    }

    public static Command from(final String input) {
        if (MOVE_FORMAT.matcher(input).matches()) {
            final List<String> commandInputs = Arrays.asList(input.split(COMMAND_DELIMITER));

            return new Command(CommandFormat.MOVE, commandInputs.subList(SOURCE_INDEX, TARGET_INDEX));
        }

        return new Command(CommandFormat.from(input));
    }

    public CommandFormat getCommandFormat() {
        return commandFormat;
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
