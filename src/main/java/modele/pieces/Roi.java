package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementCombine;
import modele.mouvement.MouvementNormal;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.Collection;

//TODO Implement castling fully (currently only allows basic without conditions)
public class Roi extends OffsetPiece {
    private static final Offset[] OFFSETS = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT,
            Offset.HAUT_DROIT,
            Offset.GAUCHE,
            Offset.DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS,
            Offset.BAS_DROIT
    };

    /**
     * Le nombre de mouvements complétés sur la pièce
     */
    private int nombresDeMouvements = 0;

    public Roi(Couleur couleur) {
        super(couleur);
    }

    @Override
    int unicodeForWhite() {
        return 9812;
    }

    @Override
    int unicodeForBlack() {
        return 9818;
    }

    @Override
    Offset[] getOffsets() {
        return OFFSETS;
    }

    @Override
    public int getValeurPositive() {
        return 1000;
    }

    @Override
    Collection<Position> generatePosition(Plateau plateau) {
        Collection<Position> positions = super.generatePosition(plateau);

        //Ajouter les options pour casteling
        Position startRoi = plateau.getPosition(this);
        Position debutTour = startRoi.decaler(new Offset(0, 3));
        Position finTour = debutTour.decaler(new Offset(0, -2));
        Position finRoi = startRoi.decaler(new Offset(0, 2));

        if (plateau.getPiece(debutTour) instanceof Tour
                && plateau.getPiece(finRoi) == null
                && plateau.getPiece(finTour) == null
                && nombresDeMouvements == 0) {

            positions.add(finRoi);
        }

        return positions;
    }

    @Override
    Mouvement convertir(Plateau plateau, Position position) {
        if (position.getColonne() - plateau.getPosition(this).getColonne() == 2)
            return new MouvementCombine(new Mouvement[]{
                    new MouvementNormal(this, position),
                    new MouvementNormal(plateau.getPiece(position.decaler(Offset.DROIT)), position.decaler(Offset.GAUCHE))
            });


        else return super.convertir(plateau, position);
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
        nombresDeMouvements += 1;
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
        nombresDeMouvements -= 1;
    }

    @Override
    String getNom() {
        return "Roi";
    }
}