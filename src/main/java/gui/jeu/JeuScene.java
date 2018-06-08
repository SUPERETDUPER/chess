package gui.jeu;

import gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import modele.Chargeur;

import java.io.IOException;

public class JeuScene {

    private final Parent root;

    public JeuScene(App.MontrerIntro goBack, Chargeur chargeur) {

        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/jeu.fxml"));
        fxmlLoader.setController(new JeuController(goBack, chargeur));

        //Charger l'interface
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chargeur.getJeu().commencer();
    }

    public Parent getRoot() {
        return root;
    }



}
