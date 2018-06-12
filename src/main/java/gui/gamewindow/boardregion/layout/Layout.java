package gui.gamewindow.boardregion.layout;

import gui.gamewindow.boardregion.components.PiecePane;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import model.util.Position;

/**
 * Représente la position d'une pièce dans l'interface gui
 * Retourne les coordonées X et Y de la position
 */
public abstract class Layout {
    /**
     * La hauteur du boardregion
     */
    final ObservableNumberValue hauteurDuPlateau;

    Layout(ObservableNumberValue hauteurDuPlateau) {
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
    public abstract void notifyPlaced(PiecePane piecePane);

    public abstract void notifyRemoved(PiecePane piecePane);
}
