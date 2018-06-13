package ui.game.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import model.util.Position;

/**
 * Controls one (of two) graveyards (region where pieces that were taken go)
 */
//TODO cleanup
public class GraveyardController {
    /**
     * How much the graveyard is offset on the X axis
     */
    private final ObservableNumberValue xOffset;

    /**
     * Whether the columns should fill left first then right or right first then left
     */
    private final boolean isLeftToRight;

    /**
     * The height of a column
     */
    private final ObservableNumberValue height;

    /**
     * The total width of the graveyard
     */
    private final NumberBinding totalWidth;

    /**
     * @param height        the height of a column
     * @param isLeftToRight Whether the columns should fill left first then right or right first then left
     */
    public GraveyardController(ObservableNumberValue height, boolean isLeftToRight) {
        this(height, isLeftToRight, new SimpleIntegerProperty(0));
    }

    /**
     * @param height        the height of a column
     * @param isLeftToRight Whether the columns should fill left first then right or right first then left
     * @param xOffset       how much the graveyard is offset on the x axis
     */
    public GraveyardController(ObservableNumberValue height, boolean isLeftToRight, ObservableNumberValue xOffset) {
        this.height = height;
        this.xOffset = xOffset;
        this.isLeftToRight = isLeftToRight;

        this.totalWidth = Bindings.multiply(height, getTotalWidthRatio());
    }

    public ObservableNumberValue getTotalWidth() {
        return totalWidth;
    }

    /**
     * @return the ratio of the width by the height
     */
    public double getTotalWidthRatio() {
        return 2.0 / Position.LIMIT;
    }

    /**
     * @param positionIndex the position in the stack of the piece (0 is the first piece)
     * @return the position of the piece with this positionIndex
     */
    public GraphicPosition getNextGraveyardPosition(int positionIndex) {
        return new GraveyardGraphicPosition(positionIndex);
    }

    /**
     * A position in the graveyard
     */
    public class GraveyardGraphicPosition implements GraphicPosition {
        /**
         * The position in the stack of the piece (one more than the past)
         */
        private final int positionIndex;

        GraveyardGraphicPosition(int positionIndex) {
            this.positionIndex = positionIndex;
        }

        @Override
        public ObservableNumberValue getX() {
            //True if we should use the left column (depends on positionIndex and isLeftToRight)
            if ((isLeftToRight && positionIndex < Position.LIMIT) ||
                    (!isLeftToRight && positionIndex >= Position.LIMIT)) {
                return xOffset;
            } else {
                return Bindings.divide(height, Position.LIMIT).add(xOffset);
            }
        }

        @Override
        public NumberBinding getY() {
            return Bindings
                    .divide(height, Position.LIMIT)
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
}
