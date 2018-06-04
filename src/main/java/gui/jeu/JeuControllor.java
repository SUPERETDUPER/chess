package gui.jeu;

import gui.App;
import gui.jeu.board.BoardController;
import gui.jeu.board.view.CustomSquarePane;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class JeuControllor {
    @FXML
    private Pane plateau;

    @FXML
    private CustomSquarePane plateauContainer;

    private final App.MontrerIntro goBack;
    private final BoardController boardController;

    public JeuControllor(App.MontrerIntro goBack, BoardController boardController) {
        this.goBack = goBack;
        this.boardController = boardController;
    }

    @FXML
    private void initialize() {
        boardController.setUp(plateau, plateauContainer);
    }

    @FXML
    private void handleBack() {
        goBack.montrerIntro();
    }
}
