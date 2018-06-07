package gui.jeu;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListView;
import gui.App;
import gui.jeu.board.PlateauPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import modele.Jeu;
import modele.joueur.Joueur;

/**
 * Controlle l'intreface de jeu
 */
class JeuController {
    @FXML
    private StackPane plateauContainer;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<Action> drawerList;

    private final PlateauPane plateauPane;

    private final ObservableList<Action> actions = FXCollections.observableArrayList();

    JeuController(App.MontrerIntro goBack, Jeu jeu) {
        this.plateauPane = new PlateauPane(jeu.getJeuData());

        for (Joueur joueur : jeu.getJoueurs().values()) {
            joueur.initializeInterface(plateauPane);
        }

        jeu.setResultatListener(resultat -> Platform.runLater(() -> handleResultat(resultat)));

        actions.add(new Action() {
            @Override
            void onClick() {
                goBack.montrerIntro();
            }

            @Override
            String getDescription() {
                return "Revenir au menu principal";
            }
        });
    }

    @FXML
    private void initialize() {
        plateauContainer.getChildren().add(plateauPane);

        drawerList.setItems(actions);

        //Quand la liste est appuyée executer l'option
        drawerList.setOnMouseClicked(event -> drawerList.getSelectionModel().getSelectedItem().onClick());
    }

    @FXML
    private void handleHamburgerClick() {
        drawer.open();
    }

    private void handleResultat(Jeu.Resultat resultat) {
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
    public abstract class Action {
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
