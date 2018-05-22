package modele.board;

public class Position {
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
}
