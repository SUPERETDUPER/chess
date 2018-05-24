package modele.moves;

import modele.board.Board;
import modele.board.Position;

public interface Move {
    Position getPositionToDisplay();

    void apply(Board board);

    void undo(Board board);
}
