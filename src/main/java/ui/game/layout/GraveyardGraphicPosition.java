package ui.game.layout;

import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;
import model.util.Colour;
import model.util.Position;

/**
 * A position in the graveyard
 */
public class GraveyardGraphicPosition implements GraphicPosition {
    /**
     * The position in the stack of the piece (one more than the past)
     */
    private final int positionIndex;

    private final Colour colour;

    private final LayoutCalculator layoutCalculator;

    GraveyardGraphicPosition(LayoutCalculator layoutCalculator, int positionIndex, Colour colour) {
        this.positionIndex = positionIndex;
        this.colour = colour;
        this.layoutCalculator = layoutCalculator;
    }

    @Override
    public ObservableNumberValue getX() {
        //True if we should use the left column (depends on positionIndex and isLeftToRight)
        if ((colour == Colour.BLACK && positionIndex < Position.LIMIT) ||
                (colour == Colour.WHITE && positionIndex >= Position.LIMIT)) {
            return layoutCalculator.getGraveyardXOffset(colour);
        } else {
            return layoutCalculator.getComponentSize().add(layoutCalculator.getGraveyardXOffset(colour));
        }
    }

    @Override
    public NumberBinding getY() {
        return layoutCalculator.getComponentSize()
                .multiply(positionIndex < Position.LIMIT ? positionIndex : positionIndex - Position.LIMIT);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof GraveyardGraphicPosition)) return false;
        return ((GraveyardGraphicPosition) obj).positionIndex == this.positionIndex;
    }

    @Override
    public String toString() {
        return "Graveyard positionIndex: " + positionIndex;
    }
}
