package modele.mouvement;

import modele.pieces.Piece;
import modele.util.Plateau;
import modele.util.Position;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un mouvement
 * Chaque mouvement a une position de départ et de fin
 */
public abstract class Mouvement implements Serializable {
    final Piece piece;
    final Position fin;

    Mouvement(Piece piece, Position fin) {
        this.piece = piece;
        this.fin = fin;
    }

    public Position getFin() {
        return fin;
    }

    /**
     * Appelé pour appliquer le mouvement sur un util de jeu
     *
     * @param plateau le util de jeu sur lequel on applique le mouvement
     */
    public void appliquer(Plateau plateau) {
        appliquerInterne(plateau);
        piece.notifyMoveCompleted(this);

    }

    abstract void appliquerInterne(Plateau plateau);

    /**
     * Appelé pour défaire un mouvement qui vient d'être appliqué sur le util de jeu
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof MouvementNormal)) return false;
        if (!this.piece.equals(((MouvementNormal) obj).piece)) return false;
        return this.fin.equals(((MouvementNormal) obj).fin);
    }

    @Override
    public String toString() {
        return piece + " à " + fin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.piece, this.fin);
    }
}
