package modele.moves;

import modele.board.Board;
import modele.board.Position;
import modele.pieces.Piece;

/**
 * Un mouvement qui mange une pièce
 */
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
        Piece piece = board.removePiece(start); //Enlève la pièce au départ
        morceauPris = board.removePiece(end); //Enlève la pièce à la fin
        board.ajouter(end, piece); //Ajoute la pièce du départ à la fin
    }

    @Override
    public void undo(Board board) {
        Piece piece = board.removePiece(end);
        board.ajouter(end, morceauPris);
        board.ajouter(start, piece);
    }
}
