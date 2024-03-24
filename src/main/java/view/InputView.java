package view;

import game.command.ChessCommand;

import java.util.Scanner;

public class InputView {

    private static final Scanner SCANNER = new Scanner(System.in);

    public ChessCommand readCommand() {
        System.out.println();

        try {
            final String input = SCANNER.nextLine();
            return CommandFormat.createCommand(input);
        } catch (final IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return readCommand();
        }
    }
}
