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
    void appliquerInterne(Plateau plateau) {
        debut = plateau.getPosition(piece); //Obtient la position initiale
        morceauPris = plateau.mangerPiece(fin); //Mange la pièce à la fin
        plateau.bougerPiece(piece, fin); //bouger la pièce
    }

    @Override
    void undoInterne(Plateau plateau) {
        plateau.bougerPiece(piece, debut);
        plateau.ajouterPiece(morceauPris, fin);
    }

    /**
     * La valeur du mouvement est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return -morceauPris.getValeur();
    }
}
