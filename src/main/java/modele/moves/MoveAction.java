package modele.moves;

import modele.board.Board;
import modele.board.Position;

public class MoveAction implements Action {
    private final Position startingPosition;
    private final Position endPosition;

    public MoveAction(Position startingPosition, Position endPosition) {
        this.startingPosition = startingPosition;
        this.endPosition = endPosition;
    }

    @Override
    public void apply(Board board) {
        board.movePiece(startingPosition, endPosition);
    }

    @Override
    public int getValue() {
        return 0;
    }
}
