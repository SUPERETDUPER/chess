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
}
