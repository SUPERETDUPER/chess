package gui.gamewindow.boardregion;

import gui.gamewindow.boardregion.components.PiecePane;
import gui.gamewindow.boardregion.layout.Layout;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contrôle les animations des pièces. (Quand les pièces glissent d'une position à l'autre)
 */
class AnimationController {
    private static final int ANIMATION_DURATION = 100;

    /**
     * La liste d'animation à effectuer
     * Chaque animation contient une pièce à déplacer et sa position finale
     */
    private final Queue<Pair<PiecePane, Layout>> animationQueue = new LinkedList<>();

    /**
     * Si une animation est en cours
     */
    private final AtomicBoolean isRunning = new AtomicBoolean(false); //Ne peut pas juste utiliser animationQueue.isEmpty();

    @Nullable
    private Runnable onFinishListener;

    void setOnFinishListener(@Nullable Runnable onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    /**
     * @param piecePane la piece à animate
     * @param position  la position où il faut bouger la pièce
     */
    void addAnimation(PiecePane piecePane, Layout position) {
        animationQueue.add(new Pair<>(piecePane, position));

        //Si rien n'est en cours notifyNextPlayer la prochaine animation et marquer comme étant en cours
        if (isRunning.compareAndSet(false, true)) {
            startNextAnimation();
        }
    }

    private void startNextAnimation() {
        Pair<PiecePane, Layout> animation = animationQueue.remove(); //Obtenir l'animation
        animate(animation.getKey(), animation.getValue()); //Animer
    }

    /**
     * Anime la pièce
     */
    private void animate(PiecePane piecePane, Layout position) {
        //Sinon
        //Créer l'animation qui change les coordonées X et Y de la pièce
        Timeline timeline = new Timeline(new KeyFrame(
                new Duration(ANIMATION_DURATION),
                new KeyValue(
                        piecePane.layoutXProperty(),
                        position.getX().getValue()
                ),
                new KeyValue(
                        piecePane.layoutYProperty(),
                        position.getY().getValue()
                )
        ));

        timeline.setOnFinished(event -> {
            piecePane.setText();
            piecePane.bind(position);

            if (animationQueue.isEmpty()) {
                isRunning.set(false); //Si il n'y a plus d'animation arrêter
                if (onFinishListener != null) {
                    onFinishListener.run();
                }
            } else startNextAnimation(); //Sinon notifyNextPlayer la prochaine animation
        });

        piecePane.unBind(); //Détacher la pièce de sa position
        timeline.play(); //Player l'animation
    }

    boolean isRunning() {
        return isRunning.get();
    }
}
