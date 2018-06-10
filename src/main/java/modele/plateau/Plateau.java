package modele.plateau;

import modele.pieces.Piece;

public interface Plateau {
    void ajouterPiece(Piece piece, Position position);

    /**
     * @return La pièce qui était à la position finale avant
     */
    Piece bougerPiece(Piece piece, Position finale);

    Position mangerPiece(Piece piece);

    Piece mangerPiece(Position position);

    Position eneleverPiece(Piece piece);

    Piece eneleverPiece(Position position);

    Position getPosition(Piece piece);
}
