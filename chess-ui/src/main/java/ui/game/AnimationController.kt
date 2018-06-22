package ui.game

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.util.Duration
import javafx.util.Pair
import ui.game.components.PiecePane
import ui.game.layout.GraphicPosition
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Controls how the pieces are animated (sliding from one position to another)
 */
internal class AnimationController {

    /**
     * The queue of animations to complete
     */
    private val animationQueue = LinkedList<Pair<PiecePane, GraphicPosition>>()

    /**
     * Indicator to now if an animation is running
     */
    internal val isRunning = AtomicBoolean(false) //Cannot rely on animationQueue,empty()

    /**
     * A method to run when all the animations are finished
     */
    private var onFinishListener: (() -> Unit)? = null

    /**
     * Sets the onFinish listener
     *
     * @param onFinishListener the Runnable to run when all the animations are finished
     */
    fun setOnFinishListener(onFinishListener: (() -> Unit)?) {
        this.onFinishListener = onFinishListener
    }

    /**
     * @param piecePane   the PiecePane to move
     * @param destination the destination of that pane
     */
    fun addAnimation(piecePane: PiecePane, destination: GraphicPosition) {
        animationQueue.add(Pair(piecePane, destination))

        //If not already running, mark as running and start animation
        if (isRunning.compareAndSet(false, true)) startNextAnimation()
    }

    private fun startNextAnimation() {
        val animation = animationQueue.remove() //Get next animation
        animate(animation.key, animation.value) //Animate
    }

    /**
     * Runs the animation
     *
     * @param piecePane   the PiecePane to move
     * @param destination the destination of that pane
     */
    private fun animate(piecePane: PiecePane, destination: GraphicPosition) {
        //Create the animation
        val animation = Timeline(KeyFrame(
                Duration(ANIMATION_DURATION.toDouble()),
                KeyValue(
                        piecePane.layoutXProperty(),
                        destination.x.value
                ),
                KeyValue(
                        piecePane.layoutYProperty(),
                        destination.y.value
                )
        ))

        animation.setOnFinished { _ -> onAnimationFinish(piecePane, destination) }

        piecePane.unBind() //Unbind to allow movement (if not throws error)
        animation.play() //Play animation
    }

    private fun onAnimationFinish(piecePane: PiecePane, destination: GraphicPosition) {
        piecePane.setText() //Update text for pawn promotion
        piecePane.bind(destination) //Bind to destination

        if (animationQueue.isEmpty()) {
            isRunning.set(false) //Mark as finished
            if (onFinishListener != null) onFinishListener!!.invoke() //Run listener
        } else
            startNextAnimation() //If more animations run next
    }

    companion object {
        private const val ANIMATION_DURATION = 100
    }
}
