package gui.jeu.board.layout;

import javafx.beans.value.ObservableValue;

public interface PositionBoard {
    ObservableValue<Number> getX();

    ObservableValue<Number> getY();

    ObservableValue<Number> getLargeur();

    void notifyPlaced();
}
