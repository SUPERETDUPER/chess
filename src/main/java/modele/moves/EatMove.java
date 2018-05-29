package modele.moves;

import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

/**
 * Un mouvement qui mange une pièce
 */
public class EatMove extends Move {
    private Piece morceauPris;

    public EatMove(Position start, Position end) {
        super(start, end);
    }

    @Override
    public void apply(Plateau plateau) {
        Piece piece = plateau.removePiece(start); //Enlève la pièce au départ
        morceauPris = plateau.removePiece(end); //Enlève la pièce à la fin
        plateau.ajouter(end, piece); //Ajoute la pièce du départ à la fin
    }

    @Override
    public void undo(Plateau plateau) {
        Piece piece = plateau.removePiece(end);
        plateau.ajouter(end, morceauPris);
        plateau.ajouter(start, piece);
    }

    @Override
    public int getValue() {
        return morceauPris.getValue() * (morceauPris.getCouleur() == Couleur.BLANC ? 1 : -1);
    }
}
