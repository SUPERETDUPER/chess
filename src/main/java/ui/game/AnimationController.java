package ui.game;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.game.components.PiecePane;
import ui.game.layout.GraphicPosition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controls how the pieces are animated (sliding from one position to another)
 */
class AnimationController {
    private static final int ANIMATION_DURATION = 100;

    /**
     * The queue of animations to complete
     */
    private final Queue<Pair<PiecePane, GraphicPosition>> animationQueue = new LinkedList<>();

    /**
     * Indicator to now if an animation is running
     */
    private final AtomicBoolean isRunning = new AtomicBoolean(false); //Ne peut pas juste utiliser animationQueue.isEmpty();

    /**
     * A method to run when all the animations are finished
     */
    @Nullable
    private Runnable onFinishListener;

    /**
     * Sets the onFinish listener
     *
     * @param onFinishListener the Runnable to run when all the animations are finished
     */
    void setOnFinishListener(@Nullable Runnable onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    /**
     * @param piecePane   the PiecePane to move
     * @param destination the destination of that pane
     */
    void addAnimation(PiecePane piecePane, GraphicPosition destination) {
        animationQueue.add(new Pair<>(piecePane, destination));

        //If not already running, mark as running and start animation
        if (isRunning.compareAndSet(false, true)) startNextAnimation();
    }

    private void startNextAnimation() {
        Pair<PiecePane, GraphicPosition> animation = animationQueue.remove(); //Get next animation
        animate(animation.getKey(), animation.getValue()); //Animate
    }

    /**
     * Runs the animation
     *
     * @param piecePane   the PiecePane to move
     * @param destination the destination of that pane
     */
    private void animate(PiecePane piecePane, GraphicPosition destination) {
        //Create the animation
        Timeline animation = new Timeline(new KeyFrame(
                new Duration(ANIMATION_DURATION),
                new KeyValue(
                        piecePane.layoutXProperty(),
                        destination.getX().getValue()
                ),
                new KeyValue(
                        piecePane.layoutYProperty(),
                        destination.getY().getValue()
                )
        ));

        animation.setOnFinished(event -> onAnimationFinish(piecePane, destination));

        piecePane.unBind(); //Unbind to allow mouvement (if not throws error)
        animation.play(); //Play animation
    }

    private void onAnimationFinish(PiecePane piecePane, @NotNull GraphicPosition destination) {
        piecePane.setText(); //Update text for pawn promotion
        piecePane.bind(destination); //Bind to destination

        if (animationQueue.isEmpty()) {
            isRunning.set(false); //Mark as finished
            if (onFinishListener != null) onFinishListener.run(); //Run listener
        } else startNextAnimation(); //If more animations run next
    }

    boolean isRunning() {
        return isRunning.get();
    }
}
