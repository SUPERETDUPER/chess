package gui.jeu.board.view;

import gui.jeu.board.DisplayCalculator;
import javafx.geometry.Orientation;
import javafx.scene.layout.StackPane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class SquareStackPane extends StackPane {
    private DisplayCalculator displayCalculator;

    public void setDisplayCalculator(DisplayCalculator displayCalculator) {
        this.displayCalculator = displayCalculator;
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré comme étant la hauteur
     */
    @Override
    protected double computePrefWidth(double height) {
        if (displayCalculator == null) {
            return height * 11 / 8;
        } else {
            return height * displayCalculator.getWidthHeightRatio();
        }
    }
}
