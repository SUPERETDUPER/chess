package gui.jeu.board;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.plateau.Position;

public class CasePosition implements PositionBoard {
    private final Position position;
    private final ObservableNumberValue hauteur;
    private final ObservableNumberValue xOffset;

    public CasePosition(Position position, ObservableNumberValue hauteur, ObservableNumberValue xOffset) {
        this.position = position;
        this.hauteur = hauteur;
        this.xOffset = xOffset;
    }

    @Override
    public ObservableValue<Number> getX() {
        return Bindings.divide(hauteur, Position.LIMITE).multiply(position.getColonne()).add(xOffset);
    }

    @Override
    public ObservableValue<Number> getY() {
        return Bindings.divide(hauteur, Position.LIMITE).multiply(position.getRangee());
    }
}
