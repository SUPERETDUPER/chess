package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.io.Serializable;
import java.util.Set;

/**
 * Une pièce de jeu
 */
public abstract class Piece implements Serializable {
    Couleur couleur;

    public Piece(Couleur couleur) {
        this.couleur = couleur;
    }

    /**
     * @return La couleur de la pièce
     */
    public Couleur getCouleur() {
        return couleur;
    }

    /**
     * @return le numéro de la pièce en unicode
     */
    public int getNumeroUnicode() {
        return couleur == Couleur.BLANC ? unicodeForWhite() : unicodeForBlack();
    }

    /**
     * @return la valeur de la pièce (négatif ou positif dépandant de la couleur)
     */
    public int getValeur() {
        return couleur == Couleur.BLANC ? getValeurPositive() : -getValeurPositive();
    }

    /**
     * @return le numéro unicode de la pièce blance
     */
    abstract int unicodeForWhite();

    /**
     * @return le numéro unicode de la pièce noire
     */
    abstract int unicodeForBlack();

    /**
     * Calcule tous les mouvements possibles pour cette pièce sans vérifier si le mouvement est legal (ex. roi est en train d'être attacké)
     *
     * @param plateau le plateau avec les mouvements présentement
     * @return tous les mouvements possibles pour cette pièce
     */
    public abstract Set<Mouvement> generateAllMoves(Plateau plateau);

    /**
     * @param plateau
     * @param position
     * @return vrai si cette pièce attack présentement cette position
     */
    public abstract boolean attaquePosition(Plateau plateau, Position position);

    /**
     * @return La valeur de la pièce indépendament de la couleur
     */
    public abstract int getValeurPositive();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "-" + (couleur == Couleur.BLANC ? "b" : "n");
    }
}
