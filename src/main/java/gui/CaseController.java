package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controle une case
 */
public class CaseController {
    public static final int HEIGHT_TO_FONT_RATIO = 2;
    @FXML
    private StackPane root;
    @FXML
    private Label label;

    private final boolean isBlanc;

    public CaseController(boolean isBlanc) {
        this.isBlanc = isBlanc;
    }

    @FXML
    private void initialize() {
        //Met la couleur blanc ou gris
        root.setBackground(new Background(new BackgroundFill(isBlanc ? Color.WHITE : Color.LIGHTGRAY, null, null)));
        root.heightProperty().addListener((observable, oldValue, newValue) -> label.setFont(Font.font(newValue.floatValue() / HEIGHT_TO_FONT_RATIO)));
    }
}
