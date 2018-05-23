package modele.moves;

import modele.board.Board;

public interface Action {
    void apply(Board board);

    int getValue();
}
