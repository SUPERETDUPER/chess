package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementNormal extends Mouvement {
    private Position debut;

    public MouvementNormal(Piece piece, Position end) {
        super(piece, end);
    }

    void appliquerInterne(Plateau plateau) {
        debut = plateau.removePiece(piece);
        Piece pieceEnlever = plateau.ajouter(fin, piece);
        if (pieceEnlever != null) throw new IllegalArgumentException("Une pièce est à cette position: " + this);
    }

    @Override
    void undoInterne(Plateau plateau) {
        plateau.bougerPiece(debut, piece);
    }

    /**
     * Le mouvement ne capture pas de pièce donc il n'y a pas de valeur
     *
     * @return 0
     */
    @Override
    public int getValeur() {
        return 0;
    }
}
