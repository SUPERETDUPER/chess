package gui.intro;

import com.jfoenix.controls.JFXComboBox;
import gui.App;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import modele.joueur.Joueur;
import modele.joueur.JoueurHumain;
import modele.joueur.JoueurOrdi;
import modele.pieces.Couleur;

import java.util.EnumMap;

/**
 * Controlle la page d'intro
 */
class IntroController {
    /**
     * La méthode à appeler pour passer au jeu
     */
    private final App.MontrerJeu onJouer;

    @FXML
    private VBox joueurBlancContainer;

    @FXML
    private VBox joueurNoirContainer;

    //Les drop-downs pour les joueurs blancs/noirs
    private JFXComboBox<Joueur> joueursBlanc = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(),
            new JoueurOrdi()
    ));

    private JFXComboBox<Joueur> joueursNoir = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(),
            new JoueurOrdi()
    ));

    IntroController(App.MontrerJeu onJouer) {
        this.onJouer = onJouer;

        joueursNoir.getSelectionModel().select(0);
        joueursBlanc.getSelectionModel().select(0);
    }

    @FXML
    private void initialize() {
        joueurBlancContainer.getChildren().add(joueursBlanc);
        joueurNoirContainer.getChildren().add(joueursNoir);
    }

    @FXML
    private void handleJouer() {
        //Créer les joueurs
        EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);
        joueurs.put(Couleur.BLANC, joueursBlanc.getSelectionModel().getSelectedItem());
        joueurs.put(Couleur.NOIR, joueursNoir.getSelectionModel().getSelectedItem());

        //Commencer le jeu
        onJouer.montrerJeu(joueurs);
    }
}