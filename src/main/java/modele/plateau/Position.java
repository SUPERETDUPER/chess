package modele.plateau;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Position {
    private static final int LIMITE = 8;

    private final int rangee;
    private final int colonne;

    public Position(int rangee, int indexColonne) {
        this.rangee = rangee;
        this.colonne = indexColonne;
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
    public Position offset(int offsetRangee, int offsetColonne) {
        return new Position(rangee + offsetRangee, colonne + offsetColonne);
    }

    /**
     * @return si la position rentre dans les limites
     */
    public boolean isValid() {
        return 0 <= rangee && rangee < LIMITE && 0 <= colonne && colonne < LIMITE;
    }

    @Contract(pure = true)
    public static int getLimite() {
        return LIMITE;
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
        return "rangee: " + rangee + " colonne: " + colonne;
    }


    @Override
    public int hashCode() {
        return Objects.hash(colonne, rangee);
    }
}
