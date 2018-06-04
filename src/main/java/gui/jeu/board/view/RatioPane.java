package gui.jeu.board.view;

import gui.jeu.board.DisplayCalculator;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class RatioPane extends Pane {
    private final DisplayCalculator displayCalculator = new DisplayCalculator(this.heightProperty());

    protected DisplayCalculator getDisplayCalculator() {
        return displayCalculator;
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
        return height * displayCalculator.getWidthHeightRatio();
    }
}
