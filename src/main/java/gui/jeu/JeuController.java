package gui.jeu;

import gui.App;
import gui.jeu.board.Board;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class JeuController {
    @FXML
    private StackPane plateauContainer;

    private final App.MontrerIntro goBack;
    private final Board board;

    public JeuController(App.MontrerIntro goBack, Board board) {
        this.goBack = goBack;
        this.board = board;
    }

    @FXML
    private void initialize() {
        plateauContainer.getChildren().add(board);
    }

    @FXML
    private void handleBack() {
        goBack.montrerIntro();
    }
}
