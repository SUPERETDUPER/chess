package model.moves;

import model.GameData;
import model.pieces.Piece;
import model.util.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A move that moves one piece to a new square (and eats the piece at that square if it exists)
 */
public class BaseMove extends Move {
    @Nullable
    private Piece eatenPiece;

    public BaseMove(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    void applyToGame(GameData data) {
        piece = data.getBoard().removePiece(start); //Remove the piece from the starting position
        eatenPiece = data.getBoard().add(end, piece); //Place the piece at the ending position and get the piece that was replaced

        if (eatenPiece != null) {
            data.getEatenPieces(eatenPiece.getColour()).push(eatenPiece); //Add the eaten to the stack in the game data
        }
    }

    @Override
    void undoToGame(GameData data) {
        data.getBoard().movePiece(start, piece); //Move the piece to its starting position

        //If a piece was eaten remove it from the stack and add it to the board
        if (eatenPiece != null) {
            data.getEatenPieces(eatenPiece.getColour()).pop();
            data.getBoard().add(end, eatenPiece);
        }
    }

    /**
     * @return 0 if no piece was eating (moving a piece does not change the board value). If a piece was eaten the value is - the piece's value
     */
    @Override
    public int getValue() {
        return eatenPiece == null ? 0 : -eatenPiece.getSignedValue();
    }
}
