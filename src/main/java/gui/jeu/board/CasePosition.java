package gui.jeu.board;

import javafx.beans.value.ObservableValue;
import modele.plateau.Position;

public class CasePosition implements PositionBoard {
    private final Position position;
    private final DisplayCalculator displayCalculator;

    public CasePosition(Position position, DisplayCalculator displayCalculator) {
        this.position = position;
        this.displayCalculator = displayCalculator;
    }

    @Override
    public ObservableValue<Number> getX() {
        return displayCalculator.getX(position);
    }

    @Override
    public ObservableValue<Number> getY() {
        return displayCalculator.getY(position);
    }
}
