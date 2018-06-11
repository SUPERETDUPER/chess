package modele.mouvement;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.util.Position;

public class MouvementPromotionManger extends MouvementManger {
    public MouvementPromotionManger(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}
