package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class MouvementNormal extends Mouvement {
    public MouvementNormal(Position start, Position end) {
        super(start, end);
    }

    public void appliquer(Plateau plateau) {
        Piece piece = plateau.removePiece(depart);
        if (plateau.getPiece(fin) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        plateau.ajouter(fin, piece);
    }

    @Override
    public void undo(Plateau plateau) {
        Piece piece = plateau.removePiece(fin);
        if (plateau.getPiece(depart) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        plateau.ajouter(depart, piece);
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
