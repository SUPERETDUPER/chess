package gui.gamewindow;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import gui.gamewindow.boardregion.BoardPane;
import gui.gamewindow.boardregion.HumanPlayer;
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

/**
 * Controlle l'intreface de gamewindow
 */
public class GameController {
    @FXML
    private StackPane plateauContainer;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<Action> drawerList;

    @FXML
    private JFXHamburger hamburger;

    private final BoardPane boardPane;

    private final ObservableList<Action> actions = FXCollections.observableArrayList();

    private final Loader loader;

    public GameController(Runnable goBack, Loader loader) {
        this.loader = loader;
        this.boardPane = new BoardPane(loader.getGame()); //Créer le boardregion pane

        int counter = 0;

        for (Player player : loader.getGame().getJoueurs().values()) {
            if (player instanceof HumanPlayer) {
                ((HumanPlayer) player).initializeInterface(boardPane);
                counter += 1;
            }
        }

        final int joueursHumain = counter;

        //Ajouter un listener pour quand la partie fini
        loader.getGame().setResultatListener(resultat -> Platform.runLater(() -> handleResultat(resultat)));

        //Ajouter le button pour revenir au menu principal
        actions.add(new Action() {
            @Override
            void onClick() {
                goBack.run();
            }

            @Override
            String getDescription() {
                return "Revenir au menu principal";
            }
        });

        //Si il y a des joueurs humains, ajouter un boutton undo
        if (joueursHumain != 0) {
            actions.add(new Action() {
                @Override
                void onClick() {
                    if (joueursHumain == 2) {
                        loader.getGame().undo(1);
                    } else {
                        loader.getGame().undo(2);
                    }
                }

                @Override
                String getDescription() {
                    return "Undo";
                }
            });
        }
    }

    @FXML
    private void initialize() {
        //Ajouter le boardregion
        plateauContainer.getChildren().add(boardPane);

        //Ajouter les actions à la liste
        drawerList.setItems(actions);

        //Quand la liste est appuyée executer l'option séléctionné
        drawerList.setOnMouseClicked(event -> {
            drawerList.getSelectionModel().getSelectedItem().onClick();
            drawerList.getSelectionModel().clearSelection();
        });

        //Animer le hamburger quand le drawer s'ouvre
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

        loader.getGame().notifierProchainJoueur();
    }

    /**
     * Quand le hamburger est appuyé, toggle le drawer
     */
    @FXML
    private void handleHamburgerClick() {
        drawer.toggle();
    }

    /**
     * Quand la partie fini montrer un pop-up
     *
     * @param resultat le résultat de la partie
     */
    private void handleResultat(Game.Resultat resultat) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        switch (resultat) {
            case EGALITE:
                alert.setContentText("Match nul");
                break;
            case NOIR_GAGNE:
                alert.setContentText("Les noirs ont gagnés");
                break;
            case BLANC_GAGNE:
                alert.setContentText("Les blancs ont gagnés");
        }

        alert.showAndWait();
    }

    /**
     * Une action dans le drawer
     */
    abstract class Action {
        /**
         * Ce qu'il faut faire pour compléter l'action
         */
        abstract void onClick();

        /**
         * @return Le nom de l'action
         */
        abstract String getDescription();

        @Override
        public String toString() {
            return getDescription();
        }
    }
}
