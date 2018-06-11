package graphique;

import com.jfoenix.controls.JFXComboBox;
import graphique.jeu.plateau.JoueurHumain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import modele.joueur.Joueur;
import modele.joueur.JoueurOrdi;
import modele.util.Couleur;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Controlle la fenêtre d'introduction
 */
public class IntroController {
    private static final ObservableList<Joueur> OPTION_JOUEURS = FXCollections.observableArrayList(
            new JoueurHumain(),
            new JoueurOrdi(JoueurOrdi.NIVEAU_FACILE),
            new JoueurOrdi(JoueurOrdi.NIVEAU_DIFFICILE)
    );
    /**
     * La méthode à appeler pour passer au jeu
     */
    private final Consumer<EnumMap<Couleur, Joueur>> onJouer;

    @FXML
    private VBox joueurBlancContainer;

    @FXML
    private VBox joueurNoirContainer;

    //Les drop-downs pour les joueurs blancs/noirs
    private final JFXComboBox<Joueur> joueursBlanc = new JFXComboBox<>(OPTION_JOUEURS);
    private final JFXComboBox<Joueur> joueursNoir = new JFXComboBox<>(OPTION_JOUEURS);

    public IntroController(Consumer<EnumMap<Couleur, Joueur>> onJouer) {
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

    /**
     * Appelé quand le boutton joueur est appuyé
     */
    @FXML
    private void handleJouer() {
        //Obtenir les 2 joueurs et les mettres dans une liste
        EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);
        joueurs.put(Couleur.BLANC, joueursBlanc.getSelectionModel().getSelectedItem());
        joueurs.put(Couleur.NOIR, joueursNoir.getSelectionModel().getSelectedItem());

        //Soumettre les joueurs
        onJouer.accept(joueurs);
    }
}