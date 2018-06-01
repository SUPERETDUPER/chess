package gui.jeu.view;

import javafx.geometry.Orientation;
import javafx.scene.layout.StackPane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class SquareStackPane extends StackPane {
    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré comme étant la hauteur
     */
    @Override
    protected double computePrefWidth(double height) {
//        noinspection SuspiciousNameCombination
        return height;
    }
}
