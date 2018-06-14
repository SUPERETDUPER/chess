package model.pieces;

import model.GameData;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A piece that can move to squares next to it (King and Knight)
 */
abstract class OffsetPiece extends Piece {
    OffsetPiece(Colour colour) {
        super(colour);
    }

    @Override
    Collection<Position> generatePossibleDestinations(@NotNull GameData gameData, @NotNull Position start) {
        Collection<Position> positions = new LinkedList<>();

        //For each possible position
        for (Offset offset : getOffsets()) {
            Position nextPosition = start.shift(offset);

            //If not valid skip
            if (!nextPosition.isValid()) continue;

            Piece piece = gameData.getBoard().getPiece(nextPosition);

            //If empty or piece other color can move
            if (piece == null || piece.getColour() != colour) positions.add(nextPosition);
        }

        return positions;
    }

    /**
     * @return les offsets où la pièce peut se décaler
     */
    @NotNull
    abstract Offset[] getOffsets();
}
