package modele.mouvement;

import modele.JeuData;
import modele.pieces.Piece;
import modele.util.Position;
import org.jetbrains.annotations.Nullable;

/**
 * Un mouvement qui mange une pièce
 */
public class MouvementNormal extends Mouvement {
    @Nullable
    private Piece morceauPris;

    private Position debut;

    public MouvementNormal(Piece piece, Position end) {
        super(piece, end);
    }

    @Override
    void appliquerInterne(JeuData data) {
        debut = data.getPlateau().removePiece(piece); //Enlève la pièce et obtient la position initiale
        morceauPris = data.getPlateau().ajouter(fin, piece); //Met la pièce à l'autre position et obtient la pièce remplacé

        if (morceauPris != null) {
            data.getEatenPieces().push(morceauPris);
        }
    }

    @Override
    void undoInterne(JeuData data) {
        if (morceauPris != null) {
            data.getEatenPieces().pop();
        }

        data.getPlateau().bougerPiece(debut, piece);

        if (morceauPris != null) {
            data.getPlateau().ajouter(fin, morceauPris);
        }
    }

    /**
     * La valeur du mouvement est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return morceauPris == null ? 0 : -morceauPris.getValeur();
    }
}
