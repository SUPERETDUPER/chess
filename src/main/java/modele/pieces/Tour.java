package modele.pieces;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

public class Tour extends DirectionPiece {
    private static final Offset[] OFFSET = {
            Offset.HAUT_CENTRE,
            Offset.MILIEU_GAUCHE,
            Offset.MILIEU_DROIT,
            Offset.BAS_CENTRE
    };

    public Tour(Couleur couleur) {
        super(couleur);
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        //Si la rangée ou colonne n'est pas identique on sait immédiatement que c'est faux (optimization)
        Position currentPosition = plateau.getPosition(this);
        if (position.getColonne() != currentPosition.getColonne() && position.getRangee() != currentPosition.getRangee())
            return false;

        return super.attaquePosition(plateau, position);
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
    Offset[] getDirections() {
        return OFFSET;
    }

    @Override
    public int getValeurPositive() {
        return 5;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
    }

    @Override
    String getNom() {
        return "Tour";
    }
}
