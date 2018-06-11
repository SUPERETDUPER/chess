package graphique.jeu.plateau;

import graphique.jeu.plateau.element.PiecePane;
import graphique.jeu.plateau.placement.PositionGraphique;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Contrôle les animations des pièces. (Quand les pièces glissent d'une position à l'autre)
 */
class AnimationController {
    private static final int DUREE_DE_CHAQUE_ANIMATION = 100;

    /**
     * La liste d'animation à effectuer
     */
    private Queue<Pair<PiecePane, PositionGraphique>> mouvementQueue = new LinkedList<>();

    private boolean isRunning = false;

    /**
     * @param piecePane la piece à animer
     * @param position  la position où il faut bouger la pièce
     */
    void ajouterAnimation(PiecePane piecePane, PositionGraphique position) {
        mouvementQueue.add(new Pair<>(piecePane, position));

        //Si rien n'est en cours commencer la prochaine animation
        if (!isRunning) {
            isRunning = true;
            commencerProchaineAnimation();
        }
    }

    private void commencerProchaineAnimation() {
        Pair<PiecePane, PositionGraphique> remove = mouvementQueue.remove();
        animer(remove.getKey(), remove.getValue());
    }

    /**
     * @param piecePane la pièce qui a été bougé
     * @param position  la nouvelle position de la pièce
     */
    private void onFinish(PiecePane piecePane, PositionGraphique position) {
        piecePane.bind(position); //Attacher la pièce à sa position

        if (mouvementQueue.isEmpty()) isRunning = false; //Si il n'y a plus d'animation arrêter
        else commencerProchaineAnimation(); //Sinon commencer la prochaine
    }

    /**
     * Anime la pièce
     */
    private void animer(PiecePane piecePane, PositionGraphique position) {
        if (piecePane.isAtPosition(position))
            onFinish(piecePane, position); //Si la pièce est déjà à la position ne rien faire
        else {
            //Sinon
            //Créer l'animation qui change les coordonées X et Y de la pièce
            Timeline timeline = new Timeline(new KeyFrame(
                    new Duration(DUREE_DE_CHAQUE_ANIMATION),
                    new KeyValue(
                            piecePane.layoutXProperty(),
                            position.getX().getValue()
                    ),
                    new KeyValue(
                            piecePane.layoutYProperty(),
                            position.getY().getValue()
                    )
            ));

            timeline.setOnFinished(event -> onFinish(piecePane, position));

            piecePane.unBind(); //Détacher la pièce de sa position
            timeline.play(); //Joueur l'animation
        }
    }
}
