package io.mr1g0r.tictactoe;

abstract class Player {
    private final Symbol symbol;

    protected Player(final Symbol symbol) {
        this.symbol = symbol;
    }

    abstract Move getMove(final Board board);

    public Symbol getSymbol() {
        return symbol;
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

        public Symbol toOppositeSymbol() {
            return this.symbol == X.toChar() ? Symbol.O : Symbol.X;
        }
    }
}
