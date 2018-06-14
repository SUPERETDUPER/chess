package model.moves;

import model.GameData;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

public class EnPassantMove extends BaseMove {
    private Piece eatenPiece;

    public EnPassantMove(@NotNull Position start, @NotNull Position destination) {
        super(start, destination);
    }

    @Override
    void applyToGame(@NotNull GameData data) {
        super.applyToGame(data);

        eatenPiece = data.getBoard().removePiece(end.shift(((Pawn) piece).BACKWARD));
        data.getEatenPieces(eatenPiece.getColour()).push(eatenPiece);
    }

    @Override
    void undoToGame(@NotNull GameData data) {
        data.getEatenPieces(eatenPiece.getColour()).pop();
        data.getBoard().add(end.shift(((Pawn) piece).BACKWARD), eatenPiece);

        super.undoToGame(data);
    }

    @Override
    public int getValue() {
        return -eatenPiece.getSignedValue();
    }
}
