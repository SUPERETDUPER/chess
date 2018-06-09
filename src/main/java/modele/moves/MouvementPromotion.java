package modele.moves;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.plateau.Position;

public class MouvementPromotion extends MouvementNormal {
    private final Reine reine;

    public MouvementPromotion(Piece piece, Position end, Reine reine) {
        super(piece, end);
        this.reine = reine;
    }

    @Override
    public int getValeur() {
        return reine.getValeur() - piece.getValeur();
    }
}
