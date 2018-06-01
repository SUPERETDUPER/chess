package gui.intro;

import gui.App;
import javafx.fxml.FXML;

import java.io.IOException;

class IntroController {
    private final App.MontrerJeu onJouer;

    public IntroController(App.MontrerJeu onJouer) {
        this.onJouer = onJouer;
    }

    @FXML
    private void handleJouer() throws IOException {
        onJouer.montrer(true, false);
    }
}
