package gui.intro;

import gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class IntroScene {

    private final Scene scene;

    public IntroScene(App.MontrerJeu montrerJeu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        fxmlLoader.setController(new IntroController(montrerJeu));

        scene = new Scene(fxmlLoader.load());
    }

    public Scene getScene() {
        return scene;
    }
}
