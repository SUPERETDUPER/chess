package graphique;

import graphique.jeu.JeuController;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import modele.Chargeur;
import modele.joueur.Joueur;
import modele.util.Couleur;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;

public class App extends Application {
    private static final String TITRE = "Échec et Mat";

    /**
     * Le chargeur de jeu
     */
    private final Chargeur chargeur = new Chargeur();

    /**
     * La scene
     */
    private final Scene scene = new Scene(new Pane());

    /**
     * Le contenu pour la page d'intro
     */
    private final Parent intro = loadFromFXML(new IntroController(this::nouveauJeu), getClass().getResource("/intro.fxml"));

    /**
     * Commence l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITRE);
        primaryStage.setScene(scene);

        //Si on peut charger et charger à fonctionné montrer le jeu
        if (chargeur.chargerDuFichier()) {
            scene.setRoot(loadFromFXML(new JeuController(() -> changerRoot(intro), chargeur), getClass().getResource("/jeu.fxml")));
        } else {
            scene.setRoot(intro); //Sinon montrer l'intro
        }

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Les joueurs pour le nouveau jeu
     */
    private void nouveauJeu(EnumMap<Couleur, Joueur> joueurs) {
        chargeur.creeNouveauJeu(joueurs);
        changerRoot(loadFromFXML(new JeuController(() -> changerRoot(intro), chargeur), getClass().getResource("/jeu.fxml")));
    }

    /**
     * Change au nouveau root avec un fade
     */
    private void changerRoot(Parent nouveauRoot) {
        Parent pastRoot = scene.getRoot();
        pastRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeOut = new FadeTransition(new Duration(1000), pastRoot);
        fadeOut.setFromValue(100);
        fadeOut.setToValue(0);

        nouveauRoot.setOpacity(0);
        nouveauRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeIn = new FadeTransition(new Duration(1000), nouveauRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(100);
        fadeIn.setOnFinished(event -> {
            nouveauRoot.setCacheHint(CacheHint.DEFAULT);
            pastRoot.setCacheHint(CacheHint.DEFAULT);
        });

        fadeOut.setOnFinished(event -> {
            scene.setRoot(nouveauRoot);
            fadeIn.play();
        });

        fadeOut.play();
    }

    /**
     * @param controller le controller à attacher
     * @param ressource  la ressource du fichier FXML
     * @return retourne le root du FXML avec le controller
     */
    private static Parent loadFromFXML(Object controller, URL ressource) {
        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(ressource);
        fxmlLoader.setController(controller); //Créer le controlleur

        //Charger l'interface
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args); //Commencer l'interface graphique
    }
}
