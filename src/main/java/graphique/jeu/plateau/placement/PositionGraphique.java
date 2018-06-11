package graphique.jeu.plateau.placement;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.util.Position;

/**
 * Calcule les coordonées pour une position
 */
public abstract class PositionGraphique {
    /**
     * La hauteur du util
     */
    final ObservableNumberValue hauteurDuPlateau;

    PositionGraphique(ObservableNumberValue hauteurDuPlateau) {
        this.hauteurDuPlateau = hauteurDuPlateau;
    }

    /**
     * @return la largeur et la hauteur de la position
     */
    public ObservableValue<Number> getTaille() {
        return Bindings.divide(hauteurDuPlateau, Position.LIMITE);
    }

    /**
     * @return la coordonée X de la position
     */
    public abstract ObservableValue<Number> getX();

    /**
     * @return la coordonée y de la position
     */
    public abstract ObservableValue<Number> getY();

    /**
     * Appelé pour indiquer qu'une pièce à été placé à cette position
     */
    public abstract void notifyPlaced();
}
