package modele.mouvement;

import modele.JeuData;
import modele.pieces.Piece;
import modele.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un mouvement. Représenté par une pièce qui se déplace vers une position finale
 */
public abstract class Mouvement implements Serializable {
    /**
     * La pièce qui se déplace
     */
    @NotNull
    final Piece piece;

    /**
     * La position finale de la pièce
     */
    @NotNull
    final Position fin;

    Mouvement(@NotNull Piece piece, @NotNull Position fin) {
        this.piece = piece;
        this.fin = fin;
    }

    public Position getFin() {
        return fin;
    }

    /**
     * Appelé pour appliquer le mouvement sur un plateau de jeu
     *
     * @param data le plateau de jeu sur lequel on applique le mouvement
     */
    public void appliquer(JeuData data) {
        appliquerInterne(data);
        piece.notifyMoveCompleted(this);
    }

    abstract void appliquerInterne(JeuData data);

    /**
     * Appelé pour défaire un mouvement qui vient d'être appliqué sur le plateau de jeu
     */
    public void undo(JeuData data) {
        undoInterne(data);
        piece.notifyMoveUndo(this);
    }

    abstract void undoInterne(JeuData data);

    /**
     * La valeur du mouvement. Une valeur négative signifie qu'une pièce blanche a été mangé
     *
     * @return la valeur du mouvement
     */
    public abstract int getValeur();

    //Si le mouvement est la même pièce à la même position le mouvement est égal

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MouvementBouger)) return false;
        if (!this.piece.equals(((MouvementBouger) obj).piece)) return false;
        return this.fin.equals(((MouvementBouger) obj).fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.piece, this.fin);
    }

    @Override
    public String toString() {
        return piece + " à " + fin;
    }
}
