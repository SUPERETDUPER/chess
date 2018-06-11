package modele.moves;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementPromotionManger extends MouvementManger {
    public MouvementPromotionManger(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    void appliquerInterne(Plateau plateau) {
        System.out.println("Appliquer");
        super.appliquerInterne(plateau);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}
