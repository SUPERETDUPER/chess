package model.moves;

import model.pieces.Queen;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

/**
 * A move that promotes a pawn to a queen.
 */
public class PromotionMove extends BaseMove {
    public PromotionMove(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    public int getValue() {
        return super.getValue() + new Queen(piece.getColour()).getSignedValue() - piece.getSignedValue();
    }
}
