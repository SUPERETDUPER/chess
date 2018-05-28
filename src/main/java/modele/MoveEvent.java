package modele;

import modele.moves.Move;

import java.util.function.Consumer;

public class MoveEvent {
    private boolean isConsumed = false;
    private final boolean isWhite;
    private final Consumer<Move> moveCallback;

    public MoveEvent(boolean isWhite, Consumer<Move> moveCallback) {
        this.isWhite = isWhite;
        this.moveCallback = moveCallback;
    }

    public void jouer(Move move) {
        isConsumed = true;
        moveCallback.accept(move);
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
