package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementNormal extends Mouvement {
    public MouvementNormal(Piece piece, Position end) {
        super(piece, end);
    }

    public void appliquer(Plateau plateau) {
        debut = plateau.removePiece(piece);
        Piece pieceEnlever = plateau.ajouter(fin, piece);
        if (pieceEnlever != null) throw new IllegalArgumentException("Une pièce est à cette position");
    }

    @Override
    public void undo(Plateau plateau) {
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
