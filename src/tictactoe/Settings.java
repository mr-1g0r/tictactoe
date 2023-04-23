package tictactoe;

public record Settings(Player playerOne, Player playerTwo) {
}

record Player (Mode mode, Difficulty difficulty, Symbol symbol) {

    enum Mode {
        COMPUTER,
        USER
    }

    enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    enum Symbol {
        X('X'),
        O('O');

        private final char symbol;

        Symbol(final char symbol) {
            this.symbol = symbol;
        }

        public char toChar() {
            return this.symbol;
        }
    }
}