package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementCombine;
import modele.mouvement.MouvementNormal;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.Set;

//TODO Implement castling
public class Roi extends OffsetPiece {
    private static final Offset[] OFFSETS = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT_CENTRE,
            Offset.HAUT_DROIT,
            Offset.MILIEU_GAUCHE,
            Offset.MILIEU_DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS_CENTRE,
            Offset.BAS_DROIT
    };

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
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        Set<Mouvement> mouvements = super.generateAllMoves(plateau);

        Position startRoi = plateau.getPosition(this);
        Position debutTour = startRoi.decaler(new Offset(0, 3));
        Position finTour = debutTour.decaler(new Offset(0, -2));
        Position finRoi = startRoi.decaler(new Offset(0, 2));

        if (plateau.getPiece(debutTour) instanceof Tour
                && plateau.getPiece(finRoi) == null
                && plateau.getPiece(finTour) == null
                && nombresDeMouvements == 0) {

            mouvements.add(new MouvementCombine(new Mouvement[]{
                            new MouvementNormal(this, startRoi.decaler(new Offset(0, 2))),
                            new MouvementNormal(plateau.getPiece(debutTour), startRoi.decaler(new Offset(0, 1)))
                    })
            );
        }

        return mouvements;
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