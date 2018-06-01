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

class IntroController {
    private final App.MontrerJeu onJouer;

    @FXML
    private VBox joueurBlancContainer;

    @FXML
    private VBox joueurNoirContainer;

    private JFXComboBox<Joueur> joueursBlanc = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(Couleur.BLANC),

            new JoueurOrdi(Couleur.BLANC)
    ));
    private JFXComboBox<Joueur> joueursNoir = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(Couleur.NOIR),
            new JoueurOrdi(Couleur.NOIR)
    ));

    IntroController(App.MontrerJeu onJouer) {
        this.onJouer = onJouer;
    }

    @FXML
    private void initialize() {
        joueursNoir.getSelectionModel().select(0);
        joueursBlanc.getSelectionModel().select(0);
        joueurBlancContainer.getChildren().add(joueursBlanc);
        joueurNoirContainer.getChildren().add(joueursNoir);
    }

    @FXML
    private void handleJouer() {
        onJouer.montrerJeu(joueursBlanc.getSelectionModel().getSelectedItem(),
                joueursNoir.getSelectionModel().getSelectedItem());
    }
}