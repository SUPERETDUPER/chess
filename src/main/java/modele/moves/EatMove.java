package modele.moves;

import modele.board.Board;
import modele.board.Position;
import modele.pieces.Piece;

public class EatMove implements Move {
    private final Position start;
    private final Position end;

    public EatMove(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Position getPositionToDisplay() {
        return end;
    }

    @Override
    public void apply(Board board) {
        Piece piece = board.removePiece(start);
        Piece morceauManger = board.removePiece(end);
        board.ajouter(end, piece);
    }
}
