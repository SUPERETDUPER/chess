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
    private Piece pieceTaken;

    public BaseMove(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    void applyToGame(GameData data) {
        piece = data.getBoard().removePiece(start); //Enlève la pièce et obtient la position initiale
        pieceTaken = data.getBoard().add(end, piece); //Met la pièce à l'autre position et obtient la pièce remplacé

        if (pieceTaken != null) {
            data.getEatenPieces().push(pieceTaken);
        }
    }

    @Override
    void undoToGame(GameData data) {
        data.getBoard().movePiece(start, piece);

        if (pieceTaken != null) {
            data.getEatenPieces().pop();
            data.getBoard().add(end, pieceTaken);
        }
    }

    /**
     * La valeur du moves est l'opposé de la valeur de la pièce mangé
     */
    @Override
    public int getValue() {
        return pieceTaken == null ? 0 : -pieceTaken.getSignedValue();
    }
}
