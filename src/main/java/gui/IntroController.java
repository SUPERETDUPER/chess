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
    private static final ObservableList<Player> PLAYER_OPTIONS = FXCollections.observableArrayList(
            new HumanPlayer(),
            new PlayerComputer(PlayerComputer.EASY),
            new PlayerComputer(PlayerComputer.HARD)
    );

    /**
     * La méthode à appeler pour passer au gamewindow
     */
    private final Consumer<EnumMap<Colour, Player>> onStart;

    @FXML
    private VBox whitePlayerContainer;

    @FXML
    private VBox blackPlayerContainer;

    //Les drop-downs pour les joueurs blancs/noirs
    private final JFXComboBox<Player> joueursBlanc = new JFXComboBox<>(PLAYER_OPTIONS);
    private final JFXComboBox<Player> joueursNoir = new JFXComboBox<>(PLAYER_OPTIONS);

    IntroController(Consumer<EnumMap<Colour, Player>> onStart) {
        this.onStart = onStart;

        //Sélectionner la première option
        joueursNoir.getSelectionModel().select(0);
        joueursBlanc.getSelectionModel().select(0);
    }

    @FXML
    private void initialize() {
        //Ajouter le drop down à l'interface
        whitePlayerContainer.getChildren().add(joueursBlanc);
        blackPlayerContainer.getChildren().add(joueursNoir);
    }

    /**
     * Appelé quand le boutton player est appuyé
     */
    @FXML
    private void handleStart() {
        //Obtenir les 2 joueurs et les mettres dans une liste
        EnumMap<Colour, Player> joueurs = new EnumMap<>(Colour.class);
        joueurs.put(Colour.WHITE, joueursBlanc.getSelectionModel().getSelectedItem());
        joueurs.put(Colour.BLACK, joueursNoir.getSelectionModel().getSelectedItem());

        //Soumettre les joueurs
        onStart.accept(joueurs);
    }
}