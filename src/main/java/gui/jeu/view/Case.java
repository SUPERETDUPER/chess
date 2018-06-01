package gui.jeu.view;

import javafx.beans.binding.NumberBinding;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Controle une case. Une case à différents stylestyles (couleur et bordure)
 */
public class Case extends Rectangle {
    /**
     * Les différentes couleurs et bordure possible pour la case
     */
    public enum Style {
        NORMAL,
        ROUGE,
        BLUE,
        BLUE_BORDURE
    }

    /**
     * Si la case est une case blanche ou noir (grise)
     */
    private final boolean isBlanc;

    /**
     * @param taille        la taille de la case
     * @param isBlanc       si la case est blanche
     */
    public Case(NumberBinding taille, boolean isBlanc) {
        super();
        this.isBlanc = isBlanc;

        this.widthProperty().bind(taille);
        this.heightProperty().bind(taille);
        this.setStrokeWidth(3);

        setStyle(Style.NORMAL);  //Met la couleur de l'arrière plan de la case
    }

    /**
     * @param style la nouvelle couleur de l'arrière plan de l'arrière plan
     */
    public void setStyle(@NotNull Style style) {
        this.setFill(getCouleurFill(style));

        if (style == Style.BLUE_BORDURE) {
            this.setStroke(Color.BLUE);
        } else {
            this.setStroke(null);
        }
    }

    @Contract(pure = true)
    private Paint getCouleurFill(@NotNull Style style) {
        switch (style) {
            case BLUE:
            case BLUE_BORDURE:
                return isBlanc ? Color.LIGHTBLUE : Color.CORNFLOWERBLUE;
            case ROUGE:
                return Color.PALEVIOLETRED;
            case NORMAL:
                return isBlanc ? Color.WHITE : Color.LIGHTGRAY;
            default:
                throw new IllegalArgumentException("Couleur de style inconnue");
        }
    }
}
