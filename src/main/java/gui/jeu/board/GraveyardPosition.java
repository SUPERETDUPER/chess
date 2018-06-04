package gui.jeu.board;


import javafx.beans.value.ObservableValue;
import modele.pieces.Couleur;

public class GraveyardPosition implements PositionBoard {
    private final Couleur couleur;
    private final DisplayCalculator displayCalculator;

    public GraveyardPosition(Couleur couleur, DisplayCalculator displayCalculator) {
        this.couleur = couleur;
        this.displayCalculator = displayCalculator;
    }

    @Override
    public ObservableValue<Number> getX() {
        return displayCalculator.getGraveyardX(couleur);
    }

    @Override
    public ObservableValue<Number> getY() {
        return displayCalculator.getGraveyardY(couleur);
    }
}
