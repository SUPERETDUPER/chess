package gui.gamewindow.boardregion;

import model.moves.Move;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Un objet qui représente une demande de moves d'un player humain sur le util de gamewindow
 * Utilisé pour demander au BoardPane de laisser le player humain effectuer un moves et capturer le résultat
 */
class MoveRequest {
    /**
     * La méthode à appeler quand le movement est décidé
     */
    @NotNull
    private final Consumer<Move> moveCallback;

    /**
     * la colour du player
     */
    @NotNull
    private final Colour colour;

    /**
     * Si la demande a été complété
     */
    private boolean isCompleted = false;

    MoveRequest(@NotNull Consumer<Move> callback, @NotNull Colour colour) {
        this.moveCallback = callback;
        this.colour = colour;
    }

    void apply(Move move) {
        isCompleted = true;
        moveCallback.accept(move);
    }

    boolean isCompleted() {
        return isCompleted;
    }

    @NotNull
    Colour getColour() {
        return colour;
    }
}
