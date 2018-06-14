package ui.game.components;

import javafx.beans.value.ObservableNumberValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ui.game.layout.SquareGraphicPosition;

import java.util.function.Consumer;

/**
 * Controls a square on the UI. The square can be highlighted blue or red.
 */
public class SquarePane extends Rectangle {
    /**
     * The different highlight modes
     */
    public enum Style {
        NORMAL,
        RED,
        BLUE
    }

    /**
     * True if the square is a white square (false if black/gray)
     */
    private final boolean isWhite;

    /**
     * @param isWhite       true if the square is a white square
     * @param clickListener the method to call when the square is clicked
     * @param position      the squares position
     * @param size          the size of the square (width/height)
     */
    public SquarePane(boolean isWhite, @NotNull Consumer<SquareGraphicPosition> clickListener, @NotNull SquareGraphicPosition position, ObservableNumberValue size) {
        super();
        this.isWhite = isWhite;

        this.widthProperty().bind(size);
        this.heightProperty().bind(size);
        this.xProperty().bind(position.getX());
        this.yProperty().bind(position.getY());

        this.setOnMouseClicked(event -> clickListener.accept(position));

        setStyle(Style.NORMAL);  //Set the background colour
    }

    /**
     * @param style the new highlight style
     */
    public void setStyle(@NotNull Style style) {
        this.setFill(getFillColour(style));
    }

    /**
     * @return the background fill colour for this highlight style
     */
    @Contract(pure = true)
    private Paint getFillColour(@NotNull Style style) {
        switch (style) {
            case BLUE:
                return isWhite ? Color.LIGHTBLUE : Color.CORNFLOWERBLUE;
            case RED:
                return Color.PALEVIOLETRED;
            case NORMAL:
                return isWhite ? Color.WHITE : Color.LIGHTGRAY;
            default:
                throw new IllegalArgumentException("Unknown highlight colour");
        }
    }
}
