package gui;

import modele.moves.Move;
import modele.pieces.Couleur;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class MoveRequest {
    @NotNull
    private final Consumer<Move> moveCallbackWrapper;
    @NotNull
    private final Couleur couleurDuTour;

    private boolean isConsumed = false;

    public MoveRequest(@NotNull Consumer<Move> callback, @NotNull Couleur couleurDuTour) {
        this.moveCallbackWrapper = callback;
        this.couleurDuTour = couleurDuTour;
    }

    void apply(Move move) {
        isConsumed = true;
        moveCallbackWrapper.accept(move);
    }

    boolean isCompleted() {
        return isConsumed;
    }

    @NotNull
    Couleur getCouleurDuTour() {
        return couleurDuTour;
    }
}
