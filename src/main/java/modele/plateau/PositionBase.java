package modele.plateau;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Une position du plateau
 */
public class PositionBase implements Serializable, Position {
    public static final int LIMITE = 8;

    private int rangee;
    private int colonne;

    public PositionBase(int rangee, int colonne) {
        this.rangee = rangee;
        this.colonne = colonne;
    }

    public PositionBase() {
    }

    public int getColonne() {
        return colonne;
    }

    public int getRangee() {
        return rangee;
    }

    /**
     * Retourne une position déplacé
     *
     * @return la nouvelle position
     */
    @NotNull
    public Position decaler(Offset offset) {
        return new PositionBase(rangee + offset.getOffsetVertical(), colonne + offset.getOffsetHorizontal());
    }

    /**
     * @return si la position rentre dans les limites
     */
    public boolean isValid() {
        return 0 <= rangee && rangee < LIMITE && 0 <= colonne && colonne < LIMITE;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Position)) return false;
        if (((Position) obj).getRangee() != this.rangee) return false;
        return ((Position) obj).getColonne() == this.colonne;
    }

    @Override
    public String toString() {
        return "r" + rangee + "c" + colonne;
    }


    @Override
    public int hashCode() {
        return Objects.hash(colonne, rangee);
    }
}
