package model.moves;

import model.pieces.Queen;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

/**
 * Un moves qui promouvoit un pion à une reine en la bougeant
 */
public class PromotionMove extends BaseMove {
    public PromotionMove(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Queen(piece.getColour()).getValeur() - piece.getValeur();
    }
}
