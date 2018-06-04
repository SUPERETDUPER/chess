package gui.jeu.board.view;

import gui.jeu.board.DisplayCalculator;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import modele.plateau.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Controle une case. Une case à différents styles (couleur et bordure)
 */
public class Case extends Rectangle {
    /**
     * Les différentes couleurs et bordure possible pour la case
     */
    public enum Style {
        NORMAL,
        ROUGE,
        BLUE
    }

    /**
     * Si la case est une case blanche ou noir (grise)
     */
    private final boolean isBlanc;

    /**
     * @param displayCalculator        la displayCalculator de la case
     * @param isBlanc       si la case est blanche
     * @param clickListener la fonction à appeler quand la case est appuyé
     * @param position la position de la case
     */
    public Case(DisplayCalculator displayCalculator, boolean isBlanc, @NotNull Consumer<Position> clickListener, @NotNull Position position) {
        super();
        this.isBlanc = isBlanc;

        this.widthProperty().bind(displayCalculator.getTaille());
        this.heightProperty().bind(displayCalculator.getTaille());
        this.xProperty().bind(displayCalculator.getX(position));
        this.yProperty().bind(displayCalculator.getY(position));
        this.setOnMouseClicked(event -> clickListener.accept(position));
        this.setStrokeWidth(3);

        setStyle(Style.NORMAL);  //Met la couleur de l'arrière plan de la case
    }

    /**
     * @param style la nouvelle couleur de l'arrière plan de l'arrière plan
     */
    public void setStyle(@NotNull Style style) {
        this.setFill(getCouleurFill(style));
    }

    /**
     * @param style le style de la case
     * @return la couleur de remplissage pour la case
     */
    @Contract(pure = true)
    private Paint getCouleurFill(@NotNull Style style) {
        switch (style) {
            case BLUE:
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
