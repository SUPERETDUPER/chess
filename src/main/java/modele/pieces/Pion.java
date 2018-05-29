package modele.pieces;

import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private boolean jumped = false;

    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    int unicodeForWhite() {
        return 9817;
    }

    @Override
    int unicodeForBlack() {
        return 9823;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        int orientation = getCouleur() == Couleur.BLANC ? 1 : -1;

        Position currentPosition = plateau.getPosition(this);

        return position.equals(currentPosition.offset(orientation, -1)) ||
                position.equals(currentPosition.offset(orientation, 1));

    }

    @Override
    public Set<Move> generateAllMoves(Plateau plateau) {
        Set<Move> moves = new HashSet<>();

        Position start = plateau.getPosition(this);

        boolean blocked = false;

        int orientation = getCouleur() == Couleur.BLANC ? 1 : -1;

        Position end = start.offset(orientation, 0);

        if (end.isValid()) {
            Piece piece = plateau.getPiece(end);
            if (piece == null) moves.add(new NormalMove(start, end));
            else {
                blocked = true;
            }
        }

        if (!blocked && !jumped) {
            end = start.offset(2 * orientation, 0);

            if (end.isValid()) {
                Piece piece = plateau.getPiece(end);
                if (piece == null) {
                    moves.add(new NormalMove(start, end) {
                        @Override
                        public void apply(Plateau plateau) {
                            jumped = true;
                            super.apply(plateau);
                        }

                        @Override
                        public void undo(Plateau plateau) {
                            jumped = false;
                            super.undo(plateau);
                        }
                    });
                }
            }
        }

        end = start.offset(orientation, -1);

        if (end.isValid()) {
            Piece piece = plateau.getPiece(end);
            if (piece != null && piece.getCouleur() != couleur) moves.add(new EatMove(start, end));
        }

        end = start.offset(orientation, 1);

        if (end.isValid()) {
            Piece piece = plateau.getPiece(end);
            if (piece != null && piece.getCouleur() != couleur) moves.add(new EatMove(start, end));
        }


        return moves;
    }

    @Override
    public int getValue() {
        return 1;
    }
}
