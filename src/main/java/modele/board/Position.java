package modele.board;

import java.util.Objects;

public class Position {
    private static final int TAILLE = 8;

    private final int indexRangee;
    private final int indexColonne;

    public Position(int indexRangee, int indexColonne) {
        this.indexRangee = indexRangee;
        this.indexColonne = indexColonne;
    }

    public int getIndexColonne() {
        return indexColonne;
    }

    public int getIndexRangee() {
        return indexRangee;
    }

    /**
     * Retourne une position déplacé
     *
     * @return la nouvelle position
     */
    public Position offset(int offsetRangee, int offsetColonne) {
        return new Position(indexRangee + offsetRangee, indexColonne + offsetColonne);
    }

    public boolean isValid() {
        return 0 <= indexRangee && indexRangee < TAILLE && 0 <= indexColonne && indexColonne < TAILLE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Position)) return false;
        if (((Position) obj).getIndexRangee() != this.indexRangee) return false;
        return ((Position) obj).getIndexColonne() == this.indexColonne;
    }

    @Override
    public String toString() {
        return "rangee: " + indexRangee + " colonne: " + indexColonne;
    }


    @Override
    public int hashCode() {
        return Objects.hash(indexColonne, indexRangee);
    }

    public static int getMax() {
        return TAILLE;
    }
}
