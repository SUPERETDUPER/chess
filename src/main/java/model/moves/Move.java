package model.moves;

import model.GameData;
import model.pieces.Piece;
import model.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A move. Each move has a start and end location. Each move has a piece (defined only when move is applied)
 */
public abstract class Move implements Serializable {
    /**
     * The piece that is moving. Gets set in method applyToGame
     */
    @Nullable
    Piece piece;

    @NotNull
    final Position start;

    @NotNull
    final Position end;

    public Move(@NotNull Position start, @NotNull Position end) {
        this.start = start;
        this.end = end;
    }

    @NotNull
    public Position getEnd() {
        return end;
    }

    /**
     * Applies this move to the game data
     */
    public void apply(GameData data) {
        applyToGame(data);
        piece.notifyMoveComplete(this);
    }

    /**
     * Implemented by subclasses to be actually applied to game data
     */
    abstract void applyToGame(GameData data);

    /**
     * Undoes the move from the game data
     */
    public void undo(GameData data) {
        undoToGame(data);
        piece.notifyMoveUndo(this);
    }

    abstract void undoToGame(GameData data);

    /**
     * @return the value of the move (difference in board value)
     */
    public abstract int getValue();

    /**
     * @return true if the obj is a move with the same start and end
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Move)) return false;
        if (!this.start.equals(((Move) obj).start)) return false;
        return this.end.equals(((Move) obj).end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.piece, this.end);
    }

    @Override
    public String toString() {
        return start + " to " + end;
    }
}
