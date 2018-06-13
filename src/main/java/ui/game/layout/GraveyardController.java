package ui.game.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import model.pieces.Piece;
import model.util.Position;
import ui.game.components.PiecePane;

import java.util.Stack;

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
     * The pieces currently in the graveyard
     */
    private final Stack<Piece> piecesInGraveyard = new Stack<>();

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
     * @return the position of the next piece to put in the graveyard
     */
    public GraphicPosition getNextGraveyardPosition() {
        return new GraveyardGraphicPosition();
    }

    /**
     * @return true if this piece is already in the graveyard
     */
    public boolean isInGraveyard(Piece piece) {
        return piecesInGraveyard.contains(piece);
    }

    /**
     * A position in the graveyard
     */
    private class GraveyardGraphicPosition implements GraphicPosition {
        /**
         * The index of the piece (one more than the past)
         */
        private final int index = piecesInGraveyard.size();

        @Override
        public ObservableNumberValue getX() {
            //True if we should use the left column (depends on index and isLeftToRight)
            if ((isLeftToRight && index < Position.LIMIT) ||
                    (!isLeftToRight && index >= Position.LIMIT)) {
                return xOffset;
            } else {
                return Bindings.divide(height, Position.LIMIT).add(xOffset);
            }
        }

        @Override
        public NumberBinding getY() {
            return Bindings
                    .divide(height, Position.LIMIT)
                    .multiply(index < Position.LIMIT ? index : index - Position.LIMIT);
        }

        @Override
        public void notifyPlaced(PiecePane piecePane) {
            piecesInGraveyard.push(piecePane.getPiece());
        }

        @Override
        public void notifyRemoved(PiecePane piecePane) {
            Piece piecePoped = piecesInGraveyard.pop();
            if (piecePoped != piecePane.getPiece()) throw new RuntimeException("Piece removed not last in stack");
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof GraveyardGraphicPosition)) return false;
            return ((GraveyardGraphicPosition) obj).index == this.index;
        }

        @Override
        public String toString() {
            return "Graveyard index: " + index;
        }
    }
}
