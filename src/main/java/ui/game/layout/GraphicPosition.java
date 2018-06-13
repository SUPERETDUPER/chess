package ui.game.layout;


import javafx.beans.value.ObservableValue;
import ui.game.components.PiecePane;

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

    //TODO Try changing with graveyard

    /**
     * Called to notify that a PiecePane has been binded to this position
     *
     * @param piecePane the bounded piecePane
     */
    void notifyPlaced(PiecePane piecePane);

    /**
     * Called to notify that a PiecePane has been UNbinded from this position
     *
     * @param piecePane the unbinded piecepane
     */
    void notifyRemoved(PiecePane piecePane);
}
