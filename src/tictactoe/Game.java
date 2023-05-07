package tictactoe;

public class Game {

    final Settings settings;

    private final Board board;

    public Game(final Settings settings) {
        this.board = Board.buildNewBoard();
        this.settings = settings;
    }

    public void play() {
        board.printGameBoard();

        var gameState = Board.State.PLAYING;
        while (gameState == Board.State.PLAYING) {
            // "Player One" Turn
            gameState = doPlayerTurn(settings.playerOne());
            if (gameState == Board.State.PLAYING) {
                // "Player Two" Turn
                gameState = doPlayerTurn(settings.playerTwo());
            }

            // show game result before quiting
            if (gameState != Board.State.PLAYING) {
                System.out.println(gameState);
            }
        }
    }

    private Board.State doPlayerTurn(final Player player) {
        Move coordinates;
        do {
            coordinates = player.getMove(board);
        } while (!board.isValidMove(coordinates));
        return board.makeMove(coordinates, player);
    }

}
