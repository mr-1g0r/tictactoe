package tictactoe;

public class ComputerPlayerHard extends ComputerPlayer {

    public static final String HARD = "hard";

    public ComputerPlayerHard(final Symbol symbol) {
        super(symbol, HARD);
    }

    @Override
    Move doGetMove(final Board board) {
        return null;
    }
}
