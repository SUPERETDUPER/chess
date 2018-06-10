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
        debut = plateau.getPosition(piece);
        plateau.bougerPiece(piece, fin);
        plateau.eneleverPiece(piece);
        reine = new Reine(piece.getCouleur());
        plateau.ajouterPiece(reine, fin);
    }

    @Override
    void undoInterne(Plateau plateau) {
        plateau.eneleverPiece(reine);
        plateau.ajouterPiece(piece, fin);
        plateau.bougerPiece(piece, debut);
    }

    @Override
    public int getValeur() {
        return reine.getValeur() - piece.getValeur();
    }
}
