package ui.game.layout;


import javafx.beans.value.ObservableValue;

/**
 * Represents the X and Y coordinates (position) of a piece
 */
public interface GraphicPosition {

    /**
     * @return the x coordinate for this position
     */
    ObservableValue<Number> getX();

    /**
     * @return the y coordinate for this position
     */
    ObservableValue<Number> getY();
}
