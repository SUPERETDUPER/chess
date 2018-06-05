package gui.jeu.board.view;

import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;

/**
 * Un grid pane qui aura toujours la même largeur que hauteur
 */
public class RatioPane extends Pane {


    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré si il y a un display calculator
     */
    @Override
    protected double computePrefWidth(double height) {
        //TODO Find better way
        return height * 1.25;
    }
}
