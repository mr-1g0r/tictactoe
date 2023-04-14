package tictactoe;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Game {
    char[][] board;
    char userSymbol;
    char botSymbol;

    enum State {
        NOT_FINISHED("Game not finished"),
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

    public Game(final String initState) {
        this.board = getNewBoard(initState);
        setPlayers(initState);
        printGameBoard();
    }

    private void setPlayers(final String initState) {
        var xPattern = Pattern.compile("X");
        var xMatcher = xPattern.matcher(initState);
        var xCount = (int) xMatcher.results().count();

        var oPattern = Pattern.compile("O");
        var oMatcher = oPattern.matcher(initState);
        var oCount = (int) oMatcher.results().count();

        if (xCount == oCount) {
            userSymbol = 'X';
            botSymbol = 'O';
        } else {
            userSymbol = 'O';
            botSymbol = 'X';
        }
    }

    private char[][] getNewBoard(final String initState) {
        var newBoard = new char[3][3];
        List<Character> flatBoardCellValues = CharBuffer.wrap(initState).chars()
                .mapToObj(Game::initStateToBoardSymbols).toList();
        Iterator<Character> flatBoardIterator = flatBoardCellValues.iterator();
        for (int row = 0; row < newBoard.length; row++) {
            for (int col = 0; col < newBoard[row].length; col++) {
                newBoard[row][col] = flatBoardIterator.next();
            }
        }
        return newBoard;
    }

    private static Character initStateToBoardSymbols(int ch) {
        return switch (ch) {
            case '_' -> ' ';
            case 'X', 'O' -> (char) ch;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    private void setCellValue(int row, int col, char value) {
        this.board[row-1][col-1] = value;
    }

    private char getCellValue(int row, int col) {
        return this.board[row-1][col-1];
    }

    private void printGameBoard() {
        System.out.println("---------");
        for (char[] chars : board) {
            System.out.printf("| %c %c %c |\n", chars[0], chars[1], chars[2]);
        }
        System.out.println("---------");
    }

    public void start() {
        var scanner = new Scanner(System.in);
        var gameState = State.NOT_FINISHED;
        while (gameState == State.NOT_FINISHED) {
            System.out.print("Enter the coordinates: ");
            var coordinatesString = scanner.nextLine();
            var validationPattern = Pattern.compile("\\d\\s\\d");
            var validationMatcher = validationPattern.matcher(coordinatesString);
            var coordinatesCount = (int) validationMatcher.results().count();

            if (coordinatesCount != 1) {
                System.out.println("You should enter numbers!");
                continue;
            }
            System.out.println();
            List<Integer> coordinates = Arrays.stream(coordinatesString.split("\\s"))
                    .map(Integer::parseInt).toList();
            int row = coordinates.get(0);
            int col = coordinates.get(1);
            if (row < 1 || row > 3 || col < 1 || col > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            var cellValue = getCellValue(row, col);
            if (cellValue != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            setCellValue(row, col, userSymbol);
            printGameBoard();

            gameState = getCurrentGameState();
            System.out.println(gameState);
        }
    }

    private State getCurrentGameState() {
        for (char[] chars : board) {
            if (chars[0] != ' ' && chars[0] == chars[1]
                    && chars[1] == chars[2]) {
                return getWinningState(chars[0]);
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col] != ' ' && board[0][col] == board[1][col]
                    && board[1][col] == board[2][col]) {
                return getWinningState(board[0][col]);
            }
        }

        if (board[1][1] != ' '
                && ((board[0][0] == board[1][1] && board[1][1] == board[2][2])
                        || (board[2][0] == board[1][1] && board[1][1] == board[0][2]))) {
            return getWinningState(board[1][1]);
        }

        var drawPattern = Pattern.compile("[OX]");
        var drawMatcher = drawPattern.matcher(Arrays.deepToString(board));
        if (drawMatcher.results().count() == 9) {
            return State.DRAW;
        }

        return State.NOT_FINISHED;
    }

    private State getWinningState(char symbol) {
        if (symbol == 'X') {
            return State.X_WINS;
        } else {
            return State.O_WINS;
        }
    }
}
