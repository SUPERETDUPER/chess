package gui.jeu.board;

import gui.jeu.board.view.PiecePane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

class AnimationController {
    private Queue<Pair<PiecePane, PositionBoard>> mouvementQueue = new LinkedList<>();

    private boolean isRunning = false;

    void addToQueue(PiecePane piecePane, PositionBoard position) {
        mouvementQueue.add(new Pair<>(piecePane, position));

        if (!isRunning) {
            callNext();
        }
    }

    private void callNext() {
        if (mouvementQueue.isEmpty()) {
            isRunning = false;
        } else {
            Pair<PiecePane, PositionBoard> remove = mouvementQueue.remove();
            bouger(remove.getKey(), remove.getValue());
        }
    }

    /**
     * Place la pièce à la position
     */
    private void bouger(PiecePane piecePane, PositionBoard position) {
        if (piecePane.isAtPosition(position)) {
            piecePane.bind(position);
            callNext();
        } else {
            Timeline timeline = new Timeline(new KeyFrame(
                    new Duration(100),
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
                piecePane.bind(position);
                callNext();
            });

            piecePane.unBind();
            isRunning = true;
            timeline.play();
        }
    }
}
