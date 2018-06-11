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

    /**
     * Créer l'interface de l'introduction
     */
    public Parent creeRoot(Consumer<EnumMap<Couleur, Joueur>> montrerJeu) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/intro.fxml"));
        fxmlLoader.setController(new IntroController(montrerJeu)); //Créer le controlleur

        Parent root;

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return root;
    }
}
