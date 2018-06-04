package gui.jeu.board;

import javafx.beans.value.ObservableValue;

public interface PositionBoard {
    ObservableValue<Number> getX();

    ObservableValue<Number> getY();
}
