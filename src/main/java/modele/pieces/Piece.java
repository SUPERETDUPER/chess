package modele.pieces;

import modele.JeuData;
import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;

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
        Set<Move> moves = generateAllMoves(jeuData.getBoard());
        Set<Move> legalMoves = new HashSet<>();

        Board tempBoard = jeuData.getBoard().getCopie();

        for (Move move : moves) {
            move.apply(tempBoard);

            if (!jeuData.roiInCheck(couleur, tempBoard)) {
                legalMoves.add(move);
            }

            move.undo(tempBoard);
        }

        return legalMoves;
    }

    public abstract Set<Move> generateAllMoves(Board board);

    public abstract boolean attacksPosition(Board board, Position position);

    public abstract int getValue();
}
