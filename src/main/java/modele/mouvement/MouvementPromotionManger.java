package modele.mouvement;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.util.Position;

/**
 * Un mouvement qui promouvoit un pion en mangeant une autre pièce
 */
public class MouvementPromotionManger extends MouvementManger {
    public MouvementPromotionManger(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}
