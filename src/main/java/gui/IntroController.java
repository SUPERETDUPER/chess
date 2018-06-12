package gui;

import com.jfoenix.controls.JFXComboBox;
import gui.gamewindow.boardregion.HumanPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import model.player.Player;
import model.player.PlayerComputer;
import model.util.Colour;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Controlle la fenêtre d'introduction
 */
class IntroController {
    private static final ObservableList<Player> OPTION_PLAYERS = FXCollections.observableArrayList(
            new HumanPlayer(),
            new PlayerComputer(PlayerComputer.NIVEAU_FACILE),
            new PlayerComputer(PlayerComputer.NIVEAU_DIFFICILE)
    );
    /**
     * La méthode à appeler pour passer au gamewindow
     */
    private final Consumer<EnumMap<Colour, Player>> onJouer;

    @FXML
    private VBox joueurBlancContainer;

    @FXML
    private VBox joueurNoirContainer;

    //Les drop-downs pour les joueurs blancs/noirs
    private final JFXComboBox<Player> joueursBlanc = new JFXComboBox<>(OPTION_PLAYERS);
    private final JFXComboBox<Player> joueursNoir = new JFXComboBox<>(OPTION_PLAYERS);

    IntroController(Consumer<EnumMap<Colour, Player>> onJouer) {
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
     * Appelé quand le boutton player est appuyé
     */
    @FXML
    private void handleJouer() {
        //Obtenir les 2 joueurs et les mettres dans une liste
        EnumMap<Colour, Player> joueurs = new EnumMap<>(Colour.class);
        joueurs.put(Colour.BLANC, joueursBlanc.getSelectionModel().getSelectedItem());
        joueurs.put(Colour.NOIR, joueursNoir.getSelectionModel().getSelectedItem());

        //Soumettre les joueurs
        onJouer.accept(joueurs);
    }
}