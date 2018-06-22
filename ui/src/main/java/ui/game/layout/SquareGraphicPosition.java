package ui.game.layout;

import javafx.beans.binding.NumberBinding;
import model.util.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The position of a square
 */
public class SquareGraphicPosition implements GraphicPosition {
    private final LayoutCalculator layoutCalculator;
    private final Position position;

    /**
     * @param position         the square's position
     * @param layoutCalculator the height of the board
     */
    SquareGraphicPosition(Position position, LayoutCalculator layoutCalculator) {
        this.layoutCalculator = layoutCalculator;
        this.position = position;
    }

    @Override
    public NumberBinding getX() {
        return layoutCalculator.getComponentSize()
                .multiply(position.getColumn()).add(layoutCalculator.getBoardXOffset());
    }

    @Override
    public NumberBinding getY() {
        return layoutCalculator.getComponentSize()
                .multiply(position.getRow());
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof SquareGraphicPosition)) return false;
        return ((SquareGraphicPosition) obj).position.equals(this.position);
    }

    @NotNull
    @Override
    public String toString() {
        return "Square: " + position;
    }
}
