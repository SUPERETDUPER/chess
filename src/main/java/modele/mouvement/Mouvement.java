package modele.mouvement;

import modele.pieces.Piece;
import modele.util.Plateau;
import modele.util.Position;
import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un mouvement. Représenté par une pièce qui se déplace vers une position finale
 */
public abstract class Mouvement implements Serializable {
    /**
     * La pièce qui se déplace
     */
    final Piece piece;

    /**
     * La position finale de la pièce
     */
    final Position fin;

    Mouvement(Piece piece, Position fin) {
        this.piece = piece;
        this.fin = fin;
    }

    public Position getFin() {
        return fin;
    }

    /**
     * Appelé pour appliquer le mouvement sur un plateau de jeu
     *
     * @param plateau le plateau de jeu sur lequel on applique le mouvement
     */
    public void appliquer(Plateau plateau) {
        appliquerInterne(plateau);
        piece.notifyMoveCompleted(this);
    }

    abstract void appliquerInterne(Plateau plateau);

    /**
     * Appelé pour défaire un mouvement qui vient d'être appliqué sur le plateau de jeu
     */
    public void undo(Plateau plateau) {
        undoInterne(plateau);
        piece.notifyMoveUndo(this);
    }

    abstract void undoInterne(Plateau plateau);

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
