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
 * Controle une case. Une case à différents styles (couleur et bordure)
 */
public class SquarePane extends Rectangle {
    /**
     * Les différentes couleurs et bordure possible pour la case
     */
    public enum Style {
        NORMAL,
        RED,
        BLUE
    }

    /**
     * Si la case est une case blanche ou noir (grise)
     */
    private final boolean isWhite;

    /**
     * @param isWhite       si la case est blanche
     * @param clickListener la fonction à appeler quand la case est appuyé
     * @param position      la position de la case
     * @param size the size of the square (width/height)
     */
    public SquarePane(boolean isWhite, @NotNull Consumer<SquareGraphicPosition> clickListener, @NotNull SquareGraphicPosition position, ObservableNumberValue size) {
        super();
        this.isWhite = isWhite;

        this.widthProperty().bind(size);
        this.heightProperty().bind(size);
        this.xProperty().bind(position.getX());
        this.yProperty().bind(position.getY());

        this.setOnMouseClicked(event -> clickListener.accept(position));

        setStyle(Style.NORMAL);  //Met la couleur de l'arrière plan de la case
    }

    /**
     * @param style la nouvelle couleur de l'arrière plan de l'arrière plan
     */
    public void setStyle(@NotNull Style style) {
        this.setFill(getFillColour(style));
    }

    /**
     * @param style le style de la case
     * @return la couleur de remplissage pour la case
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
                throw new IllegalArgumentException("Colour de style inconnue");
        }
    }
}
