package model.moves;

import model.GameData;
import model.pieces.Piece;
import model.util.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Un moves qui mange une pièce
 */
public class BaseMove extends Move {
    @Nullable
    private Piece morceauPris;

    public BaseMove(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    void appliquerInterne(GameData data) {
        piece = data.getBoardMap().removePiece(debut); //Enlève la pièce et obtient la position initiale
        morceauPris = data.getBoardMap().ajouter(fin, piece); //Met la pièce à l'autre position et obtient la pièce remplacé

        if (morceauPris != null) {
            data.getEatenPieces().push(morceauPris);
        }
    }

    @Override
    void undoInterne(GameData data) {
        data.getBoardMap().bougerPiece(debut, piece);

        if (morceauPris != null) {
            data.getEatenPieces().pop();
            data.getBoardMap().ajouter(fin, morceauPris);
        }
    }

    /**
     * La valeur du moves est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValeur() {
        return morceauPris == null ? 0 : -morceauPris.getValeur();
    }
}
