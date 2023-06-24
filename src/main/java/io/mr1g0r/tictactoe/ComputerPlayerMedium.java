package io.mr1g0r.tictactoe;

import java.util.Optional;

public class ComputerPlayerMedium extends ComputerPlayer {

    public static final String MEDIUM = "medium";

    public ComputerPlayerMedium(final Symbol symbol) {
        super(symbol, MEDIUM);
    }

    @Override
    Move doGetMove(final Board board) {
        var winningLine = getLineStats(board, this.getSymbol());
        var loosingLine = getLineStats(board, this.getSymbol().toOppositeSymbol());
        var strategy = ComputerPlayerMedium.Pair.compare(winningLine, loosingLine);

        Optional<Move> move;
        if (strategy > 0) {
            // This computer player is winning - Make a winning move
            move = board.getFirstFreeCell(winningLine.lineName());
        } else if (strategy < 0) {
            // User player is one move short from winning - Block their move
            move = board.getFirstFreeCell(loosingLine.lineName());
        } else {
            // No advantage on either side - make a random move
            move = Optional.of(getRandomMove(board));
        }

        return move.orElseThrow();
    }

    record Pair(String lineName, long count) {
        public static long compare(final Pair pairOne, final Pair pairTwo) {
            return pairOne.count() - pairTwo.count();
        }
    }

    private Pair getLineStats(final Board board, final Symbol symbol) {
        return Board.winningLines.keySet().stream()
                .map(lineName -> {
                    long symbolCount = board.getCharCountInLine(lineName, symbol.toChar());
                    long emptyCellsCount = board.getCharCountInLine(lineName, Board.EMPTY_CELL);
                    return new Pair(lineName, emptyCellsCount == 0 ? -1 : symbolCount);
                })
                .reduce((result, pair) -> pair.count() > result.count() ? pair : result)
                .orElseThrow();
    }
}
