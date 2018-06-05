package gui.jeu.board;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.plateau.Position;

public class CasePosition implements PositionBoard {
    private final Position position;
    private final ObservableNumberValue xOffset;
    private final NumberBinding largeur;

    CasePosition(Position position, ObservableNumberValue hauteur, ObservableNumberValue xOffset) {
        this.position = position;
        this.xOffset = xOffset;

        largeur = Bindings.divide(hauteur, Position.LIMITE);
    }

    @Override
    public ObservableValue<Number> getX() {
        return largeur.multiply(position.getColonne()).add(xOffset);
    }

    @Override
    public ObservableValue<Number> getY() {
        return largeur.multiply(position.getRangee());
    }

    public Position getPosition() {
        return position;
    }

    public ObservableValue<Number> getLargeur() {
        return largeur;
    }
}
