package modele.mouvement;

import modele.JeuData;
import modele.pieces.Piece;
import modele.util.Position;

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
    void appliquerInterne(JeuData data) {
        debut = data.getPlateau().removePiece(piece); //Enlève la pièce et obtient la position initiale
        morceauPris = data.getPlateau().ajouter(fin, piece); //Met la pièce à l'autre position et obtient la pièce remplacé
        if (morceauPris == null) throw new RuntimeException("rien de pris");
        data.getEatenPieces().push(morceauPris);
    }

    @Override
    void undoInterne(JeuData data) {
        data.getEatenPieces().pop();
        data.getPlateau().bougerPiece(debut, piece);
        data.getPlateau().ajouter(fin, morceauPris);
    }

    /**
     * La valeur du mouvement est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return -morceauPris.getValeur();
    }
}
