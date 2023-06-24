package io.mr1g0r.tictactoe;

import java.util.Objects;

public record Move(int row, int col) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Move) obj;
        return this.row == that.row &&
                this.col == that.col;
    }

    @Override
    public String toString() {
        return "Move[" +
                "row=" + row + ", " +
                "col=" + col + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
