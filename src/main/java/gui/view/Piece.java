package gui.view;

import javafx.scene.text.Text;

public class Piece extends Text {
    public Piece(String text, double x, double y) {
        super(text);
        this.setX(x);
        this.setY(y);
    }
}
