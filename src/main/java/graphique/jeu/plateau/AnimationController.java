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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Contrôle les animations des pièces. (Quand les pièces glissent d'une position à l'autre)
 */
class AnimationController {
    private static final int DUREE_DE_CHAQUE_ANIMATION = 100;

    /**
     * La liste d'animation à effectuer
     * Chaque animation contient une pièce à déplacer et sa position finale
     */
    private final Queue<Pair<PiecePane, PositionGraphique>> animationsQueue = new LinkedList<>();

    /**
     * Si une animation est en cours
     */
    private final AtomicBoolean animationEnCours = new AtomicBoolean(false); //Ne peut pas juste utiliser animationQueue.isEmpty();

    /**
     * @param piecePane la piece à animer
     * @param position  la position où il faut bouger la pièce
     */
    void ajouterAnimation(PiecePane piecePane, PositionGraphique position) {
        animationsQueue.add(new Pair<>(piecePane, position));

        //Si rien n'est en cours notifierProchainJoueur la prochaine animation et marquer comme étant en cours
        if (animationEnCours.compareAndSet(false, true)) {
            commencerProchaineAnimation();
        }
    }

    private void commencerProchaineAnimation() {
        Pair<PiecePane, PositionGraphique> animation = animationsQueue.remove(); //Obtenir l'animation
        animer(animation.getKey(), animation.getValue()); //Animer
    }

    /**
     * Anime la pièce
     */
    private void animer(PiecePane piecePane, PositionGraphique position) {
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

        timeline.setOnFinished(event -> {
            piecePane.setText();
            piecePane.bind(position);

            if (animationsQueue.isEmpty()) animationEnCours.set(false); //Si il n'y a plus d'animation arrêter
            else commencerProchaineAnimation(); //Sinon notifierProchainJoueur la prochaine animation
        });

        piecePane.unBind(); //Détacher la pièce de sa position
        timeline.play(); //Joueur l'animation
    }

    AtomicBoolean getAnimationEnCours() {
        return animationEnCours;
    }
}
