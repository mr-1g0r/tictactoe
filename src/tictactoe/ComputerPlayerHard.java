package tictactoe;

import java.util.*;

public class ComputerPlayerHard extends ComputerPlayer {

    public static final String HARD = "hard";

    public ComputerPlayerHard(final Symbol symbol) {
        super(symbol, HARD);
    }

    @Override
    Move doGetMove(final Board board) {
        Optional<CandidateMove> candidateMove = minimax(board, 0);
        return candidateMove.get().move();
    }

    record CandidateMove(Move move, int score) implements Comparable<CandidateMove> {

        @Override
        public int compareTo(final CandidateMove otherCandidateMove) {
            return Integer.compare(this.score(), otherCandidateMove.score());
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CandidateMove that = (CandidateMove) o;
            return move.equals(that.move);
        }

        @Override
        public int hashCode() {
            return Objects.hash(move);
        }
    }

    private Optional<CandidateMove> minimax(final Board board, int depth) {
        final var depthPlayerSymbol = depth % 2 == 0 ? getSymbol().toChar() : getSymbol().toOppositeSymbol().toChar();

        List<Move> availableMoves = board.getAllEmptyCellsFlatIndexes().stream()
                .map(board::convertCellFlatIndexToCoordinates)
                .toList();
        Set<CandidateMove> candidateMoves = new HashSet<>();
        for (Move move: availableMoves) {
            var newBoard = Board.copyBoardAddMove(board, move, depthPlayerSymbol);
            var newBoardState = newBoard.getCurrentGameState();
            if ((newBoardState == Board.State.X_WINS && depthPlayerSymbol == 'X') ||
                    (newBoardState == Board.State.O_WINS && depthPlayerSymbol == 'O')) {
                var score = depthPlayerSymbol == getSymbol().toChar() ? 1 : -1;
                return Optional.of(new CandidateMove(move, score));
            }

            var nextDepthCandidateMove = minimax(newBoard, depth + 1);
            if (nextDepthCandidateMove.isPresent()) {
                if ((depthPlayerSymbol == getSymbol().toChar() && nextDepthCandidateMove.get().score() == 1) ||
                        (depthPlayerSymbol != getSymbol().toChar() && nextDepthCandidateMove.get().score() == -1)) {
                    return nextDepthCandidateMove;
                }
            }

            if (nextDepthCandidateMove.isEmpty() || nextDepthCandidateMove.get().score() != -1) {
                candidateMoves.add(new CandidateMove(move, 0));
            }
        }

        return candidateMoves.stream().findFirst();
    }
}
