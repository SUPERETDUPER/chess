package gui.view;

import javafx.geometry.Orientation;
import javafx.scene.layout.GridPane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class SquareGridPane extends GridPane {
    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    @Override
    protected double computePrefWidth(double height) {
        //noinspection SuspiciousNameCombination
        return height;
    }
}
