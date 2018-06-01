package gui.intro;

import gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class IntroRoot {

    private final Parent scene;

    public IntroRoot(App.MontrerJeu montrerJeu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        fxmlLoader.setController(new IntroController(montrerJeu));

        scene = fxmlLoader.load();
    }

    public Parent getRoot() {
        return scene;
    }
}
