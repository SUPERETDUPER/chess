package model.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Une position du boardregion.
 * (0,0) est en haut à gauche
 */
public class Position implements Serializable {
    public static final int LIMITE = 8;

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
     * Retourne une position déplacé
     *
     * @return la nouvelle position
     */
    @NotNull
    public Position shift(Offset offset) {
        return new Position(row + offset.getVerticalShift(), column + offset.getHorizontalShift());
    }

    /**
     * @return si la position rentre dans les limites
     */
    public boolean isValid() {
        return 0 <= row && row < LIMITE && 0 <= column && column < LIMITE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Position)) return false;
        if (((Position) obj).getRow() != this.row) return false;
        return ((Position) obj).getColumn() == this.column;
    }

    @Override
    public String toString() {
        return "r" + row + "c" + column;
    }


    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}
