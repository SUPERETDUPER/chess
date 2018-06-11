package modele.moves;

import modele.pieces.Piece;
import modele.pieces.Reine;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementPromotion extends Mouvement {
    private Reine reine;
    private Position debut;

    public MouvementPromotion(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    void appliquerInterne(Plateau plateau) {
        debut = plateau.removePiece(piece);
        reine = new Reine(piece.getCouleur());
        plateau.ajouter(fin, reine);
    }

    @Override
    void undoInterne(Plateau plateau) {
        plateau.removePiece(fin);
        plateau.ajouter(debut, piece);
    }

    @Override
    public int getValeur() {
        return reine.getValeur() - piece.getValeur();
    }
}
