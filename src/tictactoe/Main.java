package tictactoe;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // write your code here
        var scanner = new Scanner(System.in);

        var menu = new Menu(scanner);
        while (menu.startNewGame()) {
            var game = new Game(menu.getSettings(), scanner);
            game.play();
        }
    }

}

class Menu {
    final Scanner scanner;
    private Settings settings;

    record CommandMatcher(Pattern pattern, Class<? extends Command> clazz) {}
    private static final Pattern START_CMD_PATTERN = Pattern.compile("start\\s(easy|user)\\s(easy|user)");
    private static final Pattern EXIT_CMD_PATTERN = Pattern.compile("exit\\b");
    private static final List<CommandMatcher> ALL_COMMANDS = List.of(
            new CommandMatcher(START_CMD_PATTERN, Start.class),
            new CommandMatcher(EXIT_CMD_PATTERN, Exit.class));


    Menu(final Scanner scanner) {
        this.scanner = scanner;
    }

    boolean startNewGame() {
        while (true) {
            System.out.print("Input command: ");
            var command = createCommand(scanner.nextLine());
            if (command.isPresent()) {
                this.settings = command.get().getSettings();
                return switch (command.get().getName()) {
                    case "start" -> true;
                    default -> false;
                };
            }
            System.out.println("Bad parameters!");
        }
    }

    public Settings getSettings() {
        return settings;
    }

    static Optional<Command> createCommand(final String commandString) {
        Optional<CommandMatcher> requestedCommand = ALL_COMMANDS.stream()
                .filter(m -> {
                    var matcher = m.pattern.matcher(commandString);
                    return matcher.find();
                }).findFirst();

        if (requestedCommand.isPresent()) {
            try {
                return Optional.of(requestedCommand.get().clazz()
                        .getConstructor(String.class)
                        .newInstance(commandString));
            } catch (Exception e) {
                System.out.println("Error > Wrong command: " + e);
            }
        }

        return Optional.empty();
    }

    interface Command {
        default String getName() {
            return getClass().getSimpleName().toLowerCase();
        }

        Settings getSettings();
    }

    static class Exit implements Command {
        public Exit(final String commandString) {}

        @Override
        public Settings getSettings() {
            return null;
        }
    }

    static class Start implements Command {
        private final Settings settings;

        public Start(final String commandString) {
            var commandMatcher = START_CMD_PATTERN.matcher(commandString);
            if (commandMatcher.matches()) {
                this.settings = new Settings(
                        new Player(getMode(commandMatcher.group(1)),
                                getDifficulty(commandMatcher.group(1)),
                                Player.Symbol.X),
                        new Player(getMode(commandMatcher.group(2)),
                                getDifficulty(commandMatcher.group(2)),
                                Player.Symbol.O)
                );
            } else {
                throw new IllegalArgumentException(commandString);
            }
        }

        private Player.Mode getMode(String param) {
            if (param.equalsIgnoreCase(String.valueOf(Player.Mode.USER))) {
                return Player.Mode.USER;
            }
            return Player.Mode.COMPUTER;
        }

        private Player.Difficulty getDifficulty(String param) {
            if (!param.equalsIgnoreCase(String.valueOf(Player.Mode.USER))) {
                return Player.Difficulty.valueOf(param.toUpperCase());
            }
            return null;
        }

        @Override
        public Settings getSettings() {
            return this.settings;
        }
    }
}
