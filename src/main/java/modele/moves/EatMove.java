package modele.moves;

import modele.board.Board;
import modele.board.Position;
import modele.pieces.Piece;

public class EatMove implements Move {
    private final Position start;
    private final Position end;
    private Piece morceauPris;

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
        morceauPris = board.removePiece(end);
        board.ajouter(end, piece);
    }

    @Override
    public void undo(Board board) {
        Piece piece = board.removePiece(end);
        board.ajouter(end, morceauPris);
        board.ajouter(start, piece);
    }
}
