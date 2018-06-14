package ui.game;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import model.Game;
import model.Loader;
import model.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Controls the game page
 */
public class GameController {
    @FXML
    private StackPane boardContainer;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<Action> drawerList;

    @FXML
    private JFXHamburger hamburger;

    @NotNull
    private final BoardPane boardPane;

    @NotNull
    private final ObservableList<Action> actions = FXCollections.observableArrayList();

    /**
     * Loads the model
     */
    @NotNull
    private final Loader loader;

    /**
     * @param exit   the method to run to exit the game and return to the main menu
     * @param loader the loader that loads the game
     */
    public GameController(@NotNull Runnable exit, @NotNull Loader loader) {
        this.loader = loader;
        this.boardPane = new BoardPane(loader.getGame()); //Create the board

        //Add a listener for when the game ends
        loader.getGame().setResultListener(result -> Platform.runLater(() -> handleGameResult(result)));

        //Count the number of human players
        int counter = 0;

        for (Player player : loader.getGame().getPlayers().values()) {
            if (player instanceof HumanPlayer) {
                ((HumanPlayer) player).attachUI(boardPane); //Attach the UI
                counter += 1;
            }
        }

        final int numberOfHumans = counter;

        //Add an action to return to the main menu
        actions.add(new Action() {
            @Override
            void onClick() {
                exit.run();
            }

            @NotNull
            @Override
            String getName() {
                return "Return to main menu";
            }
        });

        //If there are human players add an undo button
        if (numberOfHumans != 0) {
            actions.add(new Action() {
                @Override
                void onClick() {
                    if (numberOfHumans == 2) {
                        loader.getGame().undo(1); //Undo 1 turn in human vs human
                    } else {
                        loader.getGame().undo(2); //Undo 2 turns in human vs computer
                    }
                }

                @NotNull
                @Override
                String getName() {
                    return "Undo";
                }
            });
        }
    }

    @FXML
    private void initialize() {
        //Add the board to its container
        boardContainer.getChildren().add(boardPane);

        //Add the actions to the list
        drawerList.setItems(actions);

        //Add a click listener to the list to register when an item was pressed
        //TODO Change because throws error if called before item is selected
        drawerList.setOnMouseClicked(event -> {
            drawerList.getSelectionModel().getSelectedItem().onClick();
            drawerList.getSelectionModel().clearSelection();
        });

        //Animate the hamburger when the drawer opens/closes
        double animationRate = Math.abs(hamburger.getAnimation().getRate());

        drawer.setOnDrawerOpening((event) -> {
            Transition burgerAnimation = hamburger.getAnimation();
            burgerAnimation.setRate(animationRate);
            burgerAnimation.play();
        });

        drawer.setOnDrawerClosing(event -> {
            Transition burgerAnimation = hamburger.getAnimation();
            burgerAnimation.setRate(-animationRate);
            burgerAnimation.play();
        });

        loader.getGame().notifyNextPlayer(); //Start the game
    }

    @FXML
    private void handleHamburgerClick() {
        drawer.toggle(); //When the hamburger is pressed open/close the drawer
    }

    /**
     * Called when the result of the game is obtained
     */
    private void handleGameResult(Game.Result result) {
        //Create an alert
        //TODO Switch to material design guidelines for alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (result) {
            case TIE:
                alert.setContentText("Stalemate");
                break;
            case BLACK_WINS:
                alert.setContentText("Black wins");
                break;
            case WHITE_WINS:
                alert.setContentText("White wins");
        }

        alert.showAndWait();
    }

    /**
     * An action in the drawer
     */
    abstract class Action {
        /**
         * What to run when the action is clicked
         */
        abstract void onClick();

        /**
         * @return the actions name (to be displayed)
         */
        @NotNull
        abstract String getName();

        @NotNull
        @Override
        public String toString() {
            return getName(); //Defines the string to display when the item is put in the list view
        }
    }
}
