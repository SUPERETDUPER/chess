package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

/**
 * Un mouvement qui mange une pièce
 */
public class MouvementManger extends Mouvement {
    private Piece morceauPris;

    public MouvementManger(Position start, Position end) {
        super(start, end);
    }

    @Override
    public void appliquer(Plateau plateau) {
        Piece piece = plateau.removePiece(depart); //Enlève la pièce au départ
        morceauPris = plateau.removePiece(fin); //Enlève la pièce à la fin
        plateau.ajouter(fin, piece); //Ajoute la pièce du départ à la fin
    }

    @Override
    public void undo(Plateau plateau) {
        Piece piece = plateau.removePiece(fin);
        plateau.ajouter(fin, morceauPris);
        plateau.ajouter(depart, piece);
    }

    /**
     * La valeur du mouvement est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return -morceauPris.getValeur();
    }
}
