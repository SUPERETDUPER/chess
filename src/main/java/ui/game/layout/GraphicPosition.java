package ui.game.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import model.util.Position;
import ui.game.components.PiecePane;

/**
 * Represents the X and Y coordinates (position) of a piece
 */
public abstract class GraphicPosition {
    /**
     * The height of the board
     */
    final ObservableNumberValue boardHeight;

    GraphicPosition(ObservableNumberValue boardHeight) {
        this.boardHeight = boardHeight;
    }

    //TODO Move out since does not represent position

    /**
     * @return the width/height of the component
     */
    public ObservableValue<Number> getSize() {
        return Bindings.divide(boardHeight, Position.LIMIT);
    }

    /**
     * @return the x coordinate for this position
     */
    public abstract ObservableValue<Number> getX();

    /**
     * @return the y coordinate for this position
     */
    public abstract ObservableValue<Number> getY();

    //TODO Try changing with graveyard

    /**
     * Called to notify that a PiecePane has been binded to this position
     *
     * @param piecePane the bounded piecePane
     */
    public abstract void notifyPlaced(PiecePane piecePane);

    /**
     * Called to notify that a PiecePane has been UNbinded from this position
     *
     * @param piecePane the unbinded piecepane
     */
    public abstract void notifyRemoved(PiecePane piecePane);
}
