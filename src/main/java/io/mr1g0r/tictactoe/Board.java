package io.mr1g0r.tictactoe;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Board {
    public static final char EMPTY_CELL = ' ';
    final char[][] board;

    public static final Map<String, List<Integer>> winningLines;

    static {
        winningLines = Map.of("row1", List.of(0, 1, 2), // top row
                "row2", List.of(3, 4, 5), // middle row
                "row3", List.of(6, 7, 8), // bottom row
                "col1", List.of(0, 3, 6), // leftmost column
                "col2", List.of(1, 4, 7), // middle column
                "col3", List.of(2, 5, 8), // rightmost column
                "diag1", List.of(0, 4, 8), // top-left to bottom-right
                "diag2", List.of(2, 4, 6)); // top-right to bottom-left
    }

    private Board() {
        this.board = new char[3][3];
        for (char[] chars : this.board) {
            Arrays.fill(chars, EMPTY_CELL);
        }
    }

    private Board(final Board other) {
        this.board = new char[3][3];
        for (int idx = 0; idx < 3; idx++) {
            this.board[idx] = other.board[idx].clone();
        }
    }

    public static Board createBoard() {
        return new Board();
    }

    public static Board copyBoardAddMove(final Board board, final Move move, char cellValue) {
        var newBoard = new Board(board);
        newBoard.setCellValue(move, cellValue);
        return newBoard;
    }

    public boolean isValidMove(final Move move) {
        var cellValue = getCellValue(move);
        if (cellValue != EMPTY_CELL) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
        return true;
    }

    public State makeMove(final Move move, final Player player) {
        setCellValue(move, player.getSymbol().toChar());
        printGameBoard();
        return getCurrentGameState();
    }

    private void setCellValue(final Move move, char value) {
        this.board[move.row() - 1][move.col() - 1] = value;
    }

    private char getCellValue(final Move move) {
        return this.board[move.row() - 1][move.col() - 1];
    }

    public void printGameBoard() {
        System.out.println("---------");
        for (char[] rowCells : board) {
            System.out.printf("| %c %c %c |\n", rowCells[0], rowCells[1], rowCells[2]);
        }
        System.out.println("---------");
    }

    /**
     * @param lineName - Line name from the winningLines map
     */
    public List<Character> getLine(final String lineName) {
        return winningLines.get(lineName).stream()
                .map(idx -> getCellValue(convertCellFlatIndexToCoordinates(idx)))
                .collect(Collectors.toList());
    }

    /**
     * @param lineName - Line name from the winningLines map
     */
    public Optional<Move> getFirstFreeCell(final String lineName) {
        var idxWithinLine = getLine(lineName).indexOf(EMPTY_CELL);
        if (idxWithinLine >= 0) {
            var flatIdx = winningLines.get(lineName).get(idxWithinLine);
            return Optional.of(convertCellFlatIndexToCoordinates(flatIdx));
        }
        return Optional.empty();
    }

    public long getCharCountInLine(final String lineName, final char symbolChar) {
        return getLine(lineName).stream()
                .filter(cellValue -> cellValue == symbolChar)
                .count();
    }

    public List<Integer> getAllEmptyCellsFlatIndexes() {
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

    public Move convertCellFlatIndexToCoordinates(int cellFlatIndex) {
        int rowCoordinate = cellFlatIndex / 3 + 1;
        int columnCoordinate = cellFlatIndex - 3 * (cellFlatIndex / 3) + 1;
        return new Move(rowCoordinate, columnCoordinate);
    }

    public State getCurrentGameState() {
        return winningLines.values().stream()
                .filter(line -> {
                    var cellOne = convertCellFlatIndexToCoordinates(line.get(0));
                    var cellTwo = convertCellFlatIndexToCoordinates(line.get(1));
                    var cellThree = convertCellFlatIndexToCoordinates(line.get(2));

                    return getCellValue(cellOne) != EMPTY_CELL &&
                            getCellValue(cellOne) == getCellValue(cellTwo) &&
                            getCellValue(cellTwo) == getCellValue(cellThree);
                })
                .findFirst()
                .map(line -> {
                    var cellOneValue = getCellValue(convertCellFlatIndexToCoordinates(line.get(0)));
                    return getWinningState(cellOneValue);
                })
                .orElseGet(() -> {
                    var drawPattern = Pattern.compile("[OX]");
                    var drawMatcher = drawPattern.matcher(Arrays.deepToString(board));
                    return (drawMatcher.results().count() == 9) ? State.DRAW : State.PLAYING;
                });
    }

    private State getWinningState(char symbol) {
        if (symbol == 'X') {
            return State.X_WINS;
        } else {
            return State.O_WINS;
        }
    }

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

}