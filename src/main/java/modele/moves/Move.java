package modele.moves;

import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.Objects;

/**
 * Un mouvement
 * Chaque mouvement a une position de départ et de fin
 */
public abstract class Move {
    final Position depart;
    final Position fin;

    public Move(Position depart, Position fin) {
        this.depart = depart;
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
        if (!this.depart.equals(((MouvementNormal) obj).depart)) return false;
        return this.fin.equals(((MouvementNormal) obj).fin);
    }

    @Override
    public String toString() {
        return "From: " + depart + " to: " + fin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.depart, this.fin);
    }
}
