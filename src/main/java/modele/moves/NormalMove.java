package modele.moves;

import modele.board.Board;
import modele.board.Position;
import modele.pieces.Piece;

public class NormalMove extends Move {
    public NormalMove(Position start, Position end) {
        super(start, end);
    }

    public void apply(Board board) {
        Piece piece = board.removePiece(start);
        if (board.getPiece(end) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        board.ajouter(end, piece);
    }

    @Override
    public void undo(Board board) {
        Piece piece = board.removePiece(end);
        if (board.getPiece(start) != null) throw new IllegalArgumentException("Une pièce est à cette position");
        board.ajouter(start, piece);
    }

    @Override
    public int getValue() {
        return 0;
    }
}
