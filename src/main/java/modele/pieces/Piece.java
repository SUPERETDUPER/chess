package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementBouger;
import modele.mouvement.MouvementManger;
import modele.util.Couleur;
import modele.util.Plateau;
import modele.util.Position;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

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

    abstract Collection<Position> generatePosition(Plateau plateau);

    Mouvement convertir(Plateau plateau, Position position) {
        if (plateau.getPiece(position) == null) return new MouvementBouger(this, position);
        else return new MouvementManger(this, position);
    }

    public Collection<Mouvement> generateAllMoves(Plateau plateau) {
        Collection<Position> positions = generatePosition(plateau);
        Collection<Mouvement> mouvements = new LinkedList<>();

        for (Position position : positions) {
            mouvements.add(convertir(plateau, position));
        }

        return mouvements;
    }

    public boolean attaquePosition(Plateau plateau, Position position) {
        Collection<Position> positions = generatePosition(plateau);
        return positions.contains(position);
    }

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
