package gui.jeu.board.view;

import gui.jeu.board.DisplayCalculator;
import javafx.geometry.Orientation;
import javafx.scene.layout.StackPane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class CustomSquarePane extends StackPane {
    private DisplayCalculator displayCalculator;

    public void setDisplayCalculator(DisplayCalculator displayCalculator) {
        this.displayCalculator = displayCalculator;
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré si il y a un display calculator
     */
    @Override
    protected double computePrefWidth(double height) {
        return displayCalculator == null ? USE_COMPUTED_SIZE : height * displayCalculator.getWidthHeightRatio();
    }
}
