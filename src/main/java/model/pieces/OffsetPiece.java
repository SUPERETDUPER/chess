package model.pieces;

import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Un morceau qui peut attacker les positions à ses côtés (Ex. cavalier, roi)
 */
abstract class OffsetPiece extends Piece {
    OffsetPiece(Colour colour) {
        super(colour);
    }

    @Override
    Collection<Position> generatePossiblePositions(BoardMap board, Position start) {
        Collection<Position> positions = new LinkedList<>();

        //Pour chaque directions
        for (Offset offset : getOffsets()) {
            Position nextPosition = start.shift(offset);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = board.getPiece(nextPosition);

            //si il y a une pièce de la même colour à cette position, passer à la prochaine sinon on peut bouger
            if (piece == null || piece.getColour() != colour) positions.add(nextPosition);
        }

        return positions;
    }

    /**
     * @return les offsets où la pièce peut se décaler
     */
    abstract Offset[] getOffsets();
}
