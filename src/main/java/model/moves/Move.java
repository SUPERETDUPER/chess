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
    final Position debut;

    /**
     * La position finale de la pièce
     */
    @NotNull
    final Position fin;

    public Move(@NotNull Position debut, @NotNull Position fin) {
        this.debut = debut;
        this.fin = fin;
    }

    @NotNull
    public Position getFin() {
        return fin;
    }

    /**
     * Appelé pour appliquer le moves sur un boardregion de gamewindow
     *
     * @param data le boardregion de gamewindow sur lequel on applique le moves
     */
    public void appliquer(GameData data) {
        appliquerInterne(data);
        piece.notifyMoveCompleted(this);
    }

    abstract void appliquerInterne(GameData data);

    /**
     * Appelé pour défaire un moves qui vient d'être appliqué sur le boardregion de gamewindow
     */
    public void undo(GameData data) {
        undoInterne(data);
        piece.notifyMoveUndo(this);
    }

    abstract void undoInterne(GameData data);

    /**
     * La valeur du moves. Une valeur négative signifie qu'une pièce blanche a été mangé
     *
     * @return la valeur du moves
     */
    public abstract int getValeur();

    //Si le moves est la même pièce à la même position le moves est égal

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Move)) return false;
        if (!this.debut.equals(((Move) obj).debut)) return false;
        return this.fin.equals(((Move) obj).fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.piece, this.fin);
    }

    @Override
    public String toString() {
        return debut + " à " + fin;
    }
}
