package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

public class NormalMove extends Move {
    public NormalMove(Position start, Position end) {
        super(start, end);
    }

    public void apply(Plateau plateau) {
        Piece piece = plateau.removePiece(start);
        if (plateau.getPiece(end) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        plateau.ajouter(end, piece);
    }

    @Override
    public void undo(Plateau plateau) {
        Piece piece = plateau.removePiece(end);
        if (plateau.getPiece(start) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        plateau.ajouter(start, piece);
    }

    @Override
    public int getValue() {
        return 0;
    }
}
