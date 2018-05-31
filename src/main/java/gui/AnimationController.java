package gui;

import gui.view.PiecePane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.NumberBinding;
import javafx.util.Duration;
import modele.moves.Mouvement;
import modele.moves.MouvementNormal;
import modele.pieces.Piece;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AnimationController {
    private Queue<Mouvement> mouvementQueue = new LinkedList<>();
    private boolean isRunning = false;
    private final HashMap<Piece, PiecePane> piecePanes;
    private final NumberBinding taille;

    public AnimationController(HashMap<Piece, PiecePane> piecePanes, NumberBinding taille) {
        this.piecePanes = piecePanes;
        this.taille = taille;
    }

    void addToQueue(Mouvement mouvement) {
        mouvementQueue.add(mouvement);

        if (!isRunning) {
            isRunning = true;
            getAnimation(mouvementQueue.remove()).play();
        }
    }

    private Animation getAnimation(Mouvement mouvement) {
        KeyFrame keyFrame = new KeyFrame(
                new Duration(100),
                (KeyValue[]) null
        );
        Timeline timeline = new Timeline(keyFrame);

        if (mouvement instanceof MouvementNormal) {
            keyFrame.getValues().add(new KeyValue(
                    piecePanes.get(mouvement.getPiece()).layoutXProperty(),
                    taille.multiply(mouvement.getFin().getColonne()).doubleValue()
            ));

            keyFrame.getValues().add(
                    new KeyValue(
                            piecePanes.get(mouvement.getPiece()).layoutYProperty(),
                            taille.multiply(mouvement.getFin().getRangee()).doubleValue()
                    ));
        }

        timeline.setOnFinished(event -> {
            if (mouvementQueue.isEmpty()) {
                isRunning = false;
                piecePanes.get(mouvement.getPiece()).fixer(
                        taille.multiply(mouvement.getFin().getColonne()),
                        taille.multiply(mouvement.getFin().getRangee())
                );
            } else {
                getAnimation(mouvementQueue.remove()).play();
            }
        });

        return timeline;
    }
}
