package modele.moves;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.Objects;

/**
 * Un mouvement
 * Chaque mouvement a une position de départ et de fin
 */
public abstract class Mouvement {
    final Piece piece;
    final Position fin;

    public Mouvement(Piece piece, Position fin) {
        this.piece = piece;
        this.fin = fin;
    }

    public Position getFin() {
        return fin;
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * Appelé pour appliquer le mouvement sur un plateau de jeu
     *
     * @param plateau le plateau de jeu sur lequel on applique le mouvement
     */
    public abstract void appliquer(Plateau plateau);

    /**
     * Appelé pour défaire un mouvement qui vient d'être appliqué sur le plateau de jeu
     */
    public abstract void undo(Plateau plateau);

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
