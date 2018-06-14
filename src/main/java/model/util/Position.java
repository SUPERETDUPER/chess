package model.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A position (square) on the board. Top-left is (0,0)
 */
public class Position implements Serializable {
    public static final int LIMIT = 8;

    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    /**
     * @param offset how much to shift this position
     * @return a new position that is shifted from this
     */
    @NotNull
    public Position shift(@NotNull Offset offset) {
        return new Position(row + offset.getVerticalShift(), column + offset.getHorizontalShift());
    }

    /**
     * @return if the position is within the limits (is a valid position on the board)
     */
    public boolean isValid() {
        return 0 <= row && row < LIMIT && 0 <= column && column < LIMIT;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Position)) return false;
        if (((Position) obj).getRow() != this.row) return false;
        return ((Position) obj).getColumn() == this.column;
    }

    @NotNull
    @Override
    public String toString() {
        return "r" + row + "c" + column;
    }


    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}
