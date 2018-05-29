package modele.pieces;

import modele.JeuData;
import modele.moves.Move;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Une pièce de jeu
 */
public abstract class Piece {
    final Couleur couleur;

    public Piece(Couleur couleur) {
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    /**
     * @return le code de la pièce en unicode
     */
    public String getUnicode() {
        return Character.toString((char) (couleur == Couleur.BLANC ? unicodeForWhite() : unicodeForBlack()));
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();

    public Set<Move> getLegalMoves(JeuData jeuData) {
        Set<Move> moves = generateAllMoves(jeuData.getPlateau());
        Set<Move> legalMoves = new HashSet<>();

        Plateau tempPlateau = jeuData.getPlateau().getCopie();

        for (Move move : moves) {
            move.apply(tempPlateau);

            if (!jeuData.roiInCheck(couleur, tempPlateau)) {
                legalMoves.add(move);
            }

            move.undo(tempPlateau);
        }

        return legalMoves;
    }

    public abstract Set<Move> generateAllMoves(Plateau plateau);

    public abstract boolean attacksPosition(Plateau plateau, Position position);

    public abstract int getValue();
}
