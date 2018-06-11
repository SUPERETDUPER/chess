package modele.pieces;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import modele.util.Plateau;
import modele.util.Position;

import java.io.Serializable;
import java.util.Set;

/**
 * Une pièce de jeu
 */
//TODO consider changing to decorator model for more flexibility and better implementation of special cases
public abstract class Piece implements Serializable {
    final Couleur couleur;

    Piece(Couleur couleur) {
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
     * @param plateau le util avec les mouvements présentement
     * @return tous les mouvements possibles pour cette pièce
     */
    public abstract Set<Mouvement> generateAllMoves(Plateau plateau);

    /**
     * @param plateau  le plateau
     * @param position la position à vérifier
     * @return vrai si cette pièce attack présentement cette position
     */
    public abstract boolean attaquePosition(Plateau plateau, Position position);

    /**
     * @return La valeur de la pièce indépendament de la couleur
     */
    protected abstract int getValeurPositive();

    /**
     * Appelé pour signifier que le mouvement à été appliqué sur la pièce
     *
     * @param mouvement le mouvement qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveCompleted(Mouvement mouvement);

    /**
     * Appelé pour signifier que le mouvement à été appliqué sur la pièce
     *
     * @param mouvement le mouvement qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveUndo(Mouvement mouvement);

    abstract String getNom();

    @Override
    public String toString() {
        return getNom() + "-" + (couleur == Couleur.BLANC ? "b" : "n");
    }
}
