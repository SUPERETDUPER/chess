package gui.jeu;

import gui.App;
import javafx.fxml.FXML;

public class JeuControllor {
    private final App.MontrerIntro goBack;

    public JeuControllor(App.MontrerIntro goBack) {
        this.goBack = goBack;
    }

    @FXML
    private void handleBack() {
        goBack.montrerIntro();
    }
}
