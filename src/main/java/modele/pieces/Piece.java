package modele.pieces;

import modele.Jeu;
import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;

import java.util.HashSet;
import java.util.Set;

/**
 * Une pièce de jeu
 */
public abstract class Piece {
    private final boolean isWhite;

    Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    /**
     * @return le code de la pièce en unicode
     */
    public String getUnicode() {
        return Character.toString((char) (isWhite ? unicodeForWhite() : unicodeForBlack()));
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();

    public Set<Move> getLegalMoves(Jeu jeu) {
        Set<Move> moves = generateAllMoves(jeu.getBoard());
        Set<Move> legalMoves = new HashSet<>();

        Board tempBoard = jeu.getBoard().getCopie();

        for (Move move : moves) {
            move.apply(tempBoard);

            if (jeu.roiInCheck(isWhite)) {
                legalMoves.add(move);
            }

            move.undo(tempBoard);
        }

        return legalMoves;
    }

    public abstract Set<Move> generateAllMoves(Board board);

    public abstract boolean attacksPosition(Board board, Position position);

    public boolean canEat(Piece piece) {
        return piece.isWhite() != this.isWhite();
    }

    public abstract int getValue();
}
