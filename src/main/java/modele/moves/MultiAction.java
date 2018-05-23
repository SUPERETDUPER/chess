package modele.moves;

import modele.board.Board;

public class MultiAction implements Action {
    private final Action[] actions;

    public MultiAction(Action[] actions) {
        this.actions = actions;
    }

    public void apply(Board board) {
        for (Action action : actions) {
            action.apply(board);
        }
    }

    public int getValue() {
        int value = 0;
        for (Action action : actions) {
            value += action.getValue();
        }
        return value;
    }
}
