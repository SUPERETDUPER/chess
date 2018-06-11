package graphique.intro;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import modele.joueur.Joueur;
import modele.util.Couleur;

import java.io.IOException;
import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Le container pour la page d'intro
 */
public class IntroRoot {

    private final Parent root;

    /**
     * Créer la page d'intro avec le fichier fxml
     */
    public IntroRoot(Consumer<EnumMap<Couleur, Joueur>> montrerJeu) {
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
