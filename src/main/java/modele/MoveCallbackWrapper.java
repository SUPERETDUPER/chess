package modele;

import modele.moves.Move;

import java.util.function.Consumer;

public class MoveCallbackWrapper {
    private boolean isConsumed = false;
    private final Consumer<Move> moveCallback;

    public MoveCallbackWrapper(Consumer<Move> moveCallback) {
        this.moveCallback = moveCallback;
    }

    public void jouer(Move move) {
        isConsumed = true;
        moveCallback.accept(move);
    }

    public boolean isConsumed() {
        return isConsumed;
    }
}
