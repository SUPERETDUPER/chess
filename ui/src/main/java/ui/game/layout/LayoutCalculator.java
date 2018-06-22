package ui.game.layout;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import model.util.Colour;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

/**
 * Calculates the position of the elements on the display
 */
public class LayoutCalculator {
    private final NumberBinding componentSize;

    public LayoutCalculator(ReadOnlyDoubleProperty boardHeight) {
        this.componentSize = boardHeight.divide(Position.LIMIT);
    }

    public NumberBinding getComponentSize() {
        return componentSize;
    }

    @NotNull
    ObservableNumberValue getGraveyardXOffset(Colour colour) {
        return colour == Colour.WHITE ?
                new SimpleIntegerProperty(0) :
                componentSize.multiply(Position.LIMIT + 2);
    }

    ObservableNumberValue getBoardXOffset() {
        return componentSize.multiply(2);
    }

    public double getWidthRatio() {
        return 1 + (4.0 / Position.LIMIT);
    }

    @NotNull
    public GraphicPosition createGraveyardPosition(Colour colour, int position) {
        return new GraveyardGraphicPosition(this, position, colour);
    }

    @NotNull
    public SquareGraphicPosition createSquarePosition(Position position) {
        return new SquareGraphicPosition(position, this);
    }
}
