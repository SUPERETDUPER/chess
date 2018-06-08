package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

/**
 * Un mouvement qui mange une pièce
 */
public class MouvementManger extends Mouvement {
    private Piece morceauPris;
    private Position debut;

    public MouvementManger(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    public void appliquer(Plateau plateau) {
        debut = plateau.removePiece(piece); //Enlève la pièce et obtient la position initiale
        morceauPris = plateau.ajouter(fin, piece); //Met la pièce à l'autre position et obtient la pièce remplacé
    }

    @Override
    public void undo(Plateau plateau) {
        plateau.bougerPiece(debut, piece);
        plateau.ajouter(fin, morceauPris);
    }

    /**
     * La valeur du mouvement est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return -morceauPris.getValeur();
    }
}
