package modele.pieces;

import modele.moves.Mouvement;
import modele.moves.MouvementCombine;
import modele.moves.MouvementNormal;
import modele.moves.MouvementNotifyWrapper;
import modele.plateau.Offset;
import modele.plateau.Plateau;
import modele.plateau.Position;

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

    private boolean hasMoved = false;

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
                && !hasMoved) {

            mouvements.add(new MouvementNotifyWrapper<>(
                    new MouvementCombine(new Mouvement[]{
                            new MouvementNormal(startRoi, startRoi.decaler(new Offset(0, 2))),
                            new MouvementNormal(debutTour, startRoi.decaler(new Offset(0, 1)))
                    }),
                    this::onMoveApply,
                    this::onMoveUndo
            ));
        }

        return mouvements;
    }

    private Boolean onMoveApply() {
        if (hasMoved) return false;
        hasMoved = true;
        return true;
    }

    private void onMoveUndo(Boolean didChangeHasMoved) {
        if (didChangeHasMoved) hasMoved = false;
    }
}