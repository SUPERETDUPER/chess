package modele.pieces;

import modele.plateau.Plateau;
import modele.plateau.Position;

public class Tour extends DirectionPiece {
    private static final int[][] OFFSET = {
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };

    public Tour(Couleur couleur) {
        super(couleur);
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);
        if (position.getColonne() != currentPosition.getColonne() && position.getRangee() != currentPosition.getRangee())
            return false;

        return super.attacksPosition(plateau, position);
    }

    @Override
    int unicodeForWhite() {
        return 9814;
    }

    @Override
    int unicodeForBlack() {
        return 9820;
    }

    @Override
    int[][] getDirections() {
        return OFFSET;
    }

    @Override
    public int getValue() {
        return 5;
    }
}
