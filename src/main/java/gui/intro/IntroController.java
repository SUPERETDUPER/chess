package gui.intro;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import modele.joueur.Joueur;
import modele.joueur.JoueurHumain;
import modele.joueur.JoueurOrdi;
import modele.pieces.Couleur;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Controlle la page d'intro
 */
class IntroController {
    /**
     * La méthode à appeler pour passer au jeu
     */
    private final Consumer<EnumMap<Couleur, Joueur>> onJouer;

    @FXML
    private VBox joueurBlancContainer;

    @FXML
    private VBox joueurNoirContainer;

    //Les drop-downs pour les joueurs blancs/noirs
    private JFXComboBox<Joueur> joueursBlanc = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(),
            new JoueurOrdi(JoueurOrdi.NIVEAU_FACILE),
            new JoueurOrdi(JoueurOrdi.NIVEAU_DIFFICILE)
    ));

    private JFXComboBox<Joueur> joueursNoir = new JFXComboBox<>(FXCollections.observableArrayList(
            new JoueurHumain(),
            new JoueurOrdi(JoueurOrdi.NIVEAU_FACILE),
            new JoueurOrdi(JoueurOrdi.NIVEAU_DIFFICILE)
    ));

    IntroController(Consumer<EnumMap<Couleur, Joueur>> onJouer) {
        this.onJouer = onJouer;

        //Sélectionner la première option
        joueursNoir.getSelectionModel().select(0);
        joueursBlanc.getSelectionModel().select(0);
    }

    @FXML
    private void initialize() {
        //Ajouter le drop down à l'interface
        joueurBlancContainer.getChildren().add(joueursBlanc);
        joueurNoirContainer.getChildren().add(joueursNoir);
    }

    @FXML
    private void handleJouer() {
        //Obtenir les 2 joueurs et les mettres dans une liste
        EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);
        joueurs.put(Couleur.BLANC, joueursBlanc.getSelectionModel().getSelectedItem());
        joueurs.put(Couleur.NOIR, joueursNoir.getSelectionModel().getSelectedItem());

        //Commencer le jeu
        onJouer.accept(joueurs);
    }
}