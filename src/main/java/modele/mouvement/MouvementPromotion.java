package modele.mouvement;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.util.Position;

/**
 * Un mouvement qui promouvoit un pion à une reine en la bougeant
 */
public class MouvementPromotion extends MouvementNormal {
    public MouvementPromotion(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}
