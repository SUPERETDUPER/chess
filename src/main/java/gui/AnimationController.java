package gui;

import gui.view.PiecePane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.NumberBinding;
import javafx.util.Duration;
import javafx.util.Pair;
import modele.plateau.Position;

import java.util.LinkedList;
import java.util.Queue;

class AnimationController {
    private final NumberBinding taille;

    private Queue<Pair<PiecePane, Position>> mouvementQueue = new LinkedList<>();

    private boolean isRunning = false;

    AnimationController(NumberBinding taille) {
        this.taille = taille;
    }

    void addToQueue(PiecePane piecePane, Position position) {
        mouvementQueue.add(new Pair<>(piecePane, position));

        if (!isRunning) {
            callNext();
        }
    }

    private void callNext() {
        if (mouvementQueue.isEmpty()) {
            isRunning = false;
        } else {
            Pair<PiecePane, Position> remove = mouvementQueue.remove();
            bouger(remove.getKey(), remove.getValue(), this::callNext);
        }
    }

    /**
     * Place la pièce à la position
     */
    private void bouger(PiecePane piecePane, Position position, Runnable onFinish) {
        if (piecePane.isAtPosition(position)) {
            onFinish.run();
        } else {
            Timeline timeline = new Timeline(new KeyFrame(
                    new Duration(100),
                    new KeyValue(
                            piecePane.layoutXProperty(),
                            taille.multiply(position.getColonne()).getValue()
                    ),
                    new KeyValue(
                            piecePane.layoutYProperty(),
                            taille.multiply(position.getRangee()).getValue()
                    )
            ));

            timeline.setOnFinished(event -> {
                piecePane.bind(position);
                onFinish.run();
            });

            piecePane.layoutXProperty().unbind();
            piecePane.layoutYProperty().unbind();
            isRunning = true;
            timeline.play();
        }
    }
}
