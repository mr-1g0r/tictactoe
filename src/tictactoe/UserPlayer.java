package tictactoe;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserPlayer extends Player {

    private final Scanner scanner;
    public UserPlayer(final Symbol symbol) {
        super(symbol);
        this.scanner = new Scanner(System.in);
    }

    @Override
    Move getMove(final Board board) {
        while (true) {
            System.out.print("Enter the coordinates: ");
            var coordinatesString = scanner.nextLine();
            var validationPattern = Pattern.compile("\\d\\s\\d");
            var validationMatcher = validationPattern.matcher(coordinatesString);
            var coordinatesCount = (int) validationMatcher.results().count();

            if (coordinatesCount != 1) {
                System.out.println("You should enter numbers!");
                continue;
            }
            List<Integer> coordinates = Arrays.stream(coordinatesString.split("\\s"))
                    .map(Integer::parseInt).toList();
            int rowCoordinate = coordinates.get(0);
            int columnCoordinate = coordinates.get(1);
            if (rowCoordinate < 1 || rowCoordinate > 3 || columnCoordinate < 1 || columnCoordinate > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            return new Move(rowCoordinate, columnCoordinate);
        }
    }
}
