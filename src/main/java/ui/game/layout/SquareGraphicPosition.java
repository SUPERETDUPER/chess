package ui.game.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;
import model.util.Position;

/**
 * The position of a square
 */
public class SquareGraphicPosition implements GraphicPosition {
    private final Position position;
    private final ObservableNumberValue xOffset;
    private final ObservableNumberValue boardHeight;

    /**
     * @param position the square's position
     * @param height  the height of the board
     * @param xOffset  how much the board is offset on the X axis
     */
    public SquareGraphicPosition(Position position, ObservableNumberValue height, ObservableNumberValue xOffset) {
        this.boardHeight = height;
        this.position = position;
        this.xOffset = xOffset;
    }

    @Override
    public NumberBinding getX() {
        return Bindings.divide(boardHeight, Position.LIMIT)
                .multiply(position.getColumn())
                .add(xOffset);
    }

    @Override
    public NumberBinding getY() {
        return Bindings.divide(boardHeight, Position.LIMIT).multiply(position.getRow());
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof SquareGraphicPosition)) return false;
        return ((SquareGraphicPosition) obj).position.equals(this.position);
    }

    @Override
    public String toString() {
        return "Square: " + position;
    }
}
