package model.pieces;

import model.moves.BaseMove;
import model.moves.CombineBaseMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;

//TODO Implement castling fully (currently only allows basic without conditions)
public class King extends OffsetPiece {
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

    public King(Colour colour) {
        super(colour);
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
    Collection<Position> generatePosition(BoardMap boardMap, Position positionDebut) {
        Collection<Position> positions = super.generatePosition(boardMap, positionDebut);

        //Ajouter les options pour casteling
        Position debutTour = positionDebut.decaler(new Offset(0, 3));
        Position finTour = debutTour.decaler(new Offset(0, -2));
        Position finRoi = positionDebut.decaler(new Offset(0, 2));

        if (boardMap.getPiece(debutTour) instanceof Rook
                && boardMap.getPiece(finRoi) == null
                && boardMap.getPiece(finTour) == null
                && nombresDeMouvements == 0) {

            positions.add(finRoi);
        }

        return positions;
    }

    @Override
    Move convertir(BoardMap boardMap, Position debut, Position finale) {
        if (finale.getColonne() - debut.getColonne() == 2)
            return new CombineBaseMove(debut, finale, new Move[]{
                    new BaseMove(finale.decaler(Offset.DROIT), finale.decaler(Offset.GAUCHE))
            });

        return super.convertir(boardMap, debut, finale);
    }

    @Override
    public void notifyMoveCompleted(Move move) {
        nombresDeMouvements += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        nombresDeMouvements -= 1;
    }

    @Override
    String getNom() {
        return "King";
    }
}