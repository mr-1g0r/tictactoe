package tictactoe;

import java.util.Random;

public abstract class ComputerPlayer extends Player {
    private final String level;

    protected ComputerPlayer(final Symbol symbol,
                             final String level) {
        super(symbol);
        this.level = level;
    }

    @Override
    final Move getMove(final Board board) {
        System.out.printf("Making move level \"%s\"%n", level);
        return doGetMove(board);
    }

    abstract Move doGetMove(final Board board);

    protected static Move getRandomMove(final Board board) {
        var allEmptyCellsFlatIndexes = board.getAllEmptyCellsFlatIndexes();
        var randomIndexPosition = new Random(System.nanoTime())
                .nextInt(allEmptyCellsFlatIndexes.size());
        var randomEmptyCellFlatIndex = allEmptyCellsFlatIndexes.get(randomIndexPosition);

        return board.convertCellFlatIndexToCoordinates(randomEmptyCellFlatIndex);
    }
}
