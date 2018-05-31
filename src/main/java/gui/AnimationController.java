package gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import modele.moves.Mouvement;
import modele.moves.MouvementNormal;

import java.util.Queue;

public class AnimationController {
    private Queue<Mouvement> mouvementQueue;
    private boolean isRunning = false;

    void addToQueue(Mouvement mouvement) {
        mouvementQueue.add(mouvement);

        if (!isRunning) {
            getAnimation(mouvementQueue.remove()).play();
        }
    }

    Animation getAnimation(Mouvement mouvement) {
        if (mouvement instanceof MouvementNormal) {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            new Duration(100),
                            new KeyValue(
                                    piecePane.layoutXProperty(),
                                    taille.multiply(position.getColonne()).doubleValue()
                            ),
                            new KeyValue(
                                    piecePane.layoutYProperty(),
                                    taille.multiply(position.getRangee()).doubleValue()
                            )
                    )
            );
        }
    }
}
