package ui.intro;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import model.player.Player;
import model.player.PlayerComputer;
import model.util.Colour;
import ui.game.HumanPlayer;

import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Controls the main menu window
 */
public class IntroController {
    /**
     * The options for the two players
     */
    private static final ObservableList<Player> PLAYER_OPTIONS = FXCollections.observableArrayList(
            new HumanPlayer(),
            new PlayerComputer(PlayerComputer.EASY),
            new PlayerComputer(PlayerComputer.HARD)
    );

    private final Consumer<EnumMap<Colour, Player>> onStart;

    @FXML
    private VBox whitePlayerContainer;

    @FXML
    private VBox blackPlayerContainer;

    //The drop-downs to select which player is playing
    private final JFXComboBox<Player> whitePlayers = new JFXComboBox<>(PLAYER_OPTIONS);
    private final JFXComboBox<Player> blackPlayers = new JFXComboBox<>(PLAYER_OPTIONS);

    public IntroController(Consumer<EnumMap<Colour, Player>> onStart) {
        this.onStart = onStart;

        //Select the first option
        blackPlayers.getSelectionModel().select(0);
        whitePlayers.getSelectionModel().select(0);
    }

    @FXML
    private void initialize() {
        //Add the drop-downs to the interface
        whitePlayerContainer.getChildren().add(whitePlayers);
        blackPlayerContainer.getChildren().add(blackPlayers);
    }

    /**
     * Called when the start button is pressed
     */
    @FXML
    private void handleStart() {
        //Get the 2 selected players and add them to a EnumMap
        EnumMap<Colour, Player> players = new EnumMap<>(Colour.class);
        players.put(Colour.WHITE, whitePlayers.getSelectionModel().getSelectedItem());
        players.put(Colour.BLACK, blackPlayers.getSelectionModel().getSelectedItem());

        //Call the callback with the players
        onStart.accept(players);
    }
}