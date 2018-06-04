package gui.intro;

import gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Le container pour la page d'intro
 */
public class IntroRoot {

    private final Parent root;

    /**
     * Créer le contenaire
     */
    public IntroRoot(App.MontrerJeu montrerJeu) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        fxmlLoader.setController(new IntroController(montrerJeu));

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Parent getRoot() {
        return root;
    }
}
