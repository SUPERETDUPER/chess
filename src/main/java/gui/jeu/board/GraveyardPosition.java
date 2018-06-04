package gui.jeu.board;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.pieces.Couleur;

public class GraveyardPosition implements PositionBoard {
    private final Couleur couleur;
    private final DisplayCalculator displayCalculator;
    private final ObservableNumberValue hauteur;

    public GraveyardPosition(Couleur couleur, DisplayCalculator displayCalculator) {
        this.couleur = couleur;
        this.displayCalculator = displayCalculator;
    }

    @Override
    public ObservableValue<Number> getX() {
        if (couleur == Couleur.BLANC) {
            return new SimpleIntegerProperty(0);
        } else {
            return Bindings.add(hauteur, displayCalculator.spacingWidth).add(displayCalculator.spacingWidth).add(displayCalculator.taille);
        }
    }

    @Override
    public ObservableValue<Number> getY() {
        return displayCalculator.getGraveyardY(couleur);
    }
}
