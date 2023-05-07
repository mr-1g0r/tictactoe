package tictactoe;

public class ComputerPlayerEasy extends ComputerPlayer {

    public static final String EASY = "easy";

    public ComputerPlayerEasy(final Symbol symbol) {
        super(symbol, EASY);
    }

    @Override
    Move doGetMove(final Board board) {
        return getRandomMove(board);
    }

}
