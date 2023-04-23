package tictactoe;

import java.util.*;
import java.util.regex.Pattern;

public class Game {
    private static final char EMPTY_CELL = ' ';
    final Settings settings;

    private final char[][] board;
    private final Scanner scanner;

    enum State {
        PLAYING("Game not finished"),
        DRAW("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins");

        private final String message;

        State(final String s) {
            this.message = s;
        }

        @Override
        public String toString() {
            return this.message;
        }
    }

    public Game(final Settings settings, final Scanner scanner) {
        this.board = getNewBoard();
        this.settings = settings;
        this.scanner = scanner;
    }

    private char[][] getNewBoard() {
        var newBoard = new char[3][3];
        for (char[] chars : newBoard) {
            Arrays.fill(chars, EMPTY_CELL);
        }
        return newBoard;
    }

    private void setCellValue(final Pair<Integer> coordinates, char value) {
        this.board[coordinates.row()-1][coordinates.col()-1] = value;
    }

    private char getCellValue(final Pair<Integer> coordinates) {
        return this.board[coordinates.row()-1][coordinates.col()-1];
    }

    private void printGameBoard() {
        System.out.println("---------");
        for (char[] rowCells : board) {
            System.out.printf("| %c %c %c |\n", rowCells[0], rowCells[1], rowCells[2]);
        }
        System.out.println("---------");
    }

    public void play() {
        printGameBoard();

        var gameState = State.PLAYING;
        while (gameState == State.PLAYING) {
            // "Player One" Turn
            gameState = doPlayerTurn(gameState, settings.playerOne());
            if (gameState == State.PLAYING) {
                // "Player Two" Turn
                gameState = doPlayerTurn(gameState, settings.playerTwo());
            }

            // show game result before quiting
            if (gameState != State.PLAYING) {
                System.out.println(gameState);
            }
        }
    }

    private State doPlayerTurn(final State oldGameState, final Player player) {
        Pair<Integer> coordinates;
        if (player.mode() == Player.Mode.USER) {
            coordinates = getUserCoordinates();
        } else {
            coordinates = getComputerCoordinates();
        }
        if (isValidMove(coordinates)) {
            return makeMove(coordinates, player); // new Game State
        }
        return oldGameState;
    }

    private State makeMove(final Pair<Integer> coordinates, final Player player) {
        if (player.mode() == Player.Mode.COMPUTER) {
            System.out.printf("Making move level \"%s\"%n", player.difficulty().toString().toLowerCase());
        }
        setCellValue(coordinates, player.symbol().toChar());
        printGameBoard();
        return getCurrentGameState();
    }

    private boolean isValidMove(final Pair<Integer> coordinates) {
        var cellValue = getCellValue(coordinates);
        if (cellValue != EMPTY_CELL) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    private Pair<Integer> getUserCoordinates() {
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
            return new Pair<>(rowCoordinate, columnCoordinate);
        }
    }

    private Pair<Integer> getComputerCoordinates() {
        var allEmptyCellsFlatIndexes = getAllEmptyCellsFlatIndexes();
        var randomIndexPosition = new Random(System.nanoTime())
                .nextInt(allEmptyCellsFlatIndexes.size());
        var randomEmptyCellFlatIndex = allEmptyCellsFlatIndexes.get(randomIndexPosition);
        return convertCellFlatIndexToCoordinates(randomEmptyCellFlatIndex);
    }

    private Pair<Integer> convertCellFlatIndexToCoordinates(int cellFlatIndex) {
        int rowCoordinate = cellFlatIndex / 3 + 1;
        int columnCoordinate = cellFlatIndex - 3 * (cellFlatIndex / 3) + 1;
        return new Pair<>(rowCoordinate, columnCoordinate);
    }

    private List<Integer> getAllEmptyCellsFlatIndexes() {
        var emptyCellsFlatIndexes = new ArrayList<Integer>();
        for (int rowIdx = 0; rowIdx < board.length; rowIdx++) {
            for (int colIdx = 0; colIdx < board[rowIdx].length; colIdx++) {
                if (board[rowIdx][colIdx] == EMPTY_CELL) {
                    var cellFlatCoordinate = 3 * rowIdx + colIdx;
                    emptyCellsFlatIndexes.add(cellFlatCoordinate);
                }
            }
        }
        return emptyCellsFlatIndexes;
    }

    private State getCurrentGameState() {
        for (char[] rowCells : board) {
            if (rowCells[0] != EMPTY_CELL && rowCells[0] == rowCells[1]
                    && rowCells[1] == rowCells[2]) {
                return getWinningState(rowCells[0]);
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col] != EMPTY_CELL && board[0][col] == board[1][col]
                    && board[1][col] == board[2][col]) {
                return getWinningState(board[0][col]);
            }
        }

        if (board[1][1] != EMPTY_CELL
                && ((board[0][0] == board[1][1] && board[1][1] == board[2][2])
                        || (board[2][0] == board[1][1] && board[1][1] == board[0][2]))) {
            return getWinningState(board[1][1]);
        }

        var drawPattern = Pattern.compile("[OX]");
        var drawMatcher = drawPattern.matcher(Arrays.deepToString(board));
        if (drawMatcher.results().count() == 9) {
            return State.DRAW;
        }

        return State.PLAYING;
    }

    private State getWinningState(char symbol) {
        if (symbol == 'X') {
            return State.X_WINS;
        } else {
            return State.O_WINS;
        }
    }

    record Pair<T>(T row, T col) {
    }
}
