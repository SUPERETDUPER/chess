package modele.mouvement;

import modele.JeuData;
import modele.pieces.Piece;
import modele.util.Position;
import org.jetbrains.annotations.NotNull;

/**
 * Un mouvement qui bouge une pièce
 */
public class MouvementBouger extends Mouvement {
    private Position debut;

    public MouvementBouger(@NotNull Piece piece, @NotNull Position end) {
        super(piece, end);
    }

    void appliquerInterne(JeuData data) {
        debut = data.getPlateau().removePiece(piece);
        Piece pieceEnlever = data.getPlateau().ajouter(fin, piece);
        if (pieceEnlever != null) throw new IllegalArgumentException("Une pièce est à cette position: " + this);
    }

    @Override
    void undoInterne(JeuData data) {
        data.getPlateau().bougerPiece(debut, piece);
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
