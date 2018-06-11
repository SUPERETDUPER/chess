package modele.moves;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementPromotion extends MouvementNormal {
    public MouvementPromotion(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    void appliquerInterne(Plateau plateau) {
        System.out.println("Appliquer");
        super.appliquerInterne(plateau);
    }

    @Override
    public int getValeur() {
        return new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}