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
 * Un moves. Représenté par une pièce qui se déplace vers une position finale
 */
public abstract class Move implements Serializable {
    /**
     * La pièce qui se déplace
     */
    @Nullable
    Piece piece;

    @NotNull
    final Position start;

    /**
     * La position finale de la pièce
     */
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
     * Appelé pour apply le moves sur un boardregion de game
     *
     * @param data le boardregion de game sur lequel on applique le moves
     */
    public void apply(GameData data) {
        applyToGame(data);
        piece.notifyMoveComplete(this);
    }

    abstract void applyToGame(GameData data);

    /**
     * Appelé pour défaire un moves qui vient d'être appliqué sur le boardregion de game
     */
    public void undo(GameData data) {
        undoToGame(data);
        piece.notifyMoveUndo(this);
    }

    abstract void undoToGame(GameData data);

    /**
     * La valeur du moves. Une valeur négative signifie qu'une pièce blanche a été mangé
     *
     * @return la valeur du moves
     */
    public abstract int getValue();

    //Si le moves est la même pièce à la même position le moves est égal

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
        return start + " à " + end;
    }
}
