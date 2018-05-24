package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private boolean jumped = false;

    public Pion(boolean isWhite) {
        super(isWhite);
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
    public Set<Move> generateMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position start = board.getPosition(this);

        boolean blocked = false;

        int orientation = isWhite() ? 1 : -1;

        Position end = start.offset(orientation, 0);

        if (end.isValid()) {
            Piece piece = board.getPiece(end);
            if (piece == null) moves.add(new NormalMove(start, end));
            else {
                blocked = true;
            }
        }

        if (!blocked && !jumped) {
            end = start.offset(2 * orientation, 0);

            if (end.isValid()) {
                Piece piece = board.getPiece(end);
                if (piece == null) {
                    moves.add(new NormalMove(start, end));
                    jumped = true;
                }
            }
        }

        end = start.offset(orientation, -1);

        if (end.isValid()) {
            Piece piece = board.getPiece(end);
            if (piece != null && canEat(piece)) moves.add(new EatMove(start, end));
        }

        end = start.offset(orientation, 1);

        if (end.isValid()) {
            Piece piece = board.getPiece(end);
            if (piece != null && canEat(piece)) moves.add(new EatMove(start, end));
        }


        return moves;
    }
}
