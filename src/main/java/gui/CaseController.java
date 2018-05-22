package gui;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Controle une case
 */
public class CaseController {
    @FXML
    private StackPane root;

    private final boolean isBlanc;

    public CaseController(boolean isBlanc) {
        this.isBlanc = isBlanc;
    }

    @FXML
    private void initialize(){
        //Met la couleur blanc ou gris
        root.setBackground(new Background(new BackgroundFill(isBlanc ? Color.WHITE : Color.LIGHTGRAY, null,null)));
    }
}
