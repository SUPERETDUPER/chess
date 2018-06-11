package graphique.jeu;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import graphique.App;
import graphique.jeu.plateau.PlateauPane;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import modele.Chargeur;
import modele.Jeu;
import modele.joueur.Joueur;
import modele.joueur.JoueurHumain;

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

    @FXML
    private JFXHamburger hamburger;

    private final PlateauPane plateauPane;

    private final ObservableList<Action> actions = FXCollections.observableArrayList();

    JeuController(App.MontrerIntro goBack, Chargeur chargeur) {
        this.plateauPane = new PlateauPane(chargeur.getJeu().getJeuData());

        int counter = 0;

        for (Joueur joueur : chargeur.getJeu().getJoueurs().values()) {
            joueur.initializeInterface(plateauPane);

            if (joueur instanceof JoueurHumain) {
                counter += 1;
            }
        }

        final int joueursHumain = counter;

        chargeur.getJeu().setResultatListener(resultat -> Platform.runLater(() -> handleResultat(resultat)));

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

        if (joueursHumain != 0) {
            actions.add(new Action() {
                @Override
                void onClick() {
                    if (joueursHumain == 2) {
                        chargeur.getJeu().undo(1);
                    } else {
                        chargeur.getJeu().undo(2);
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
        plateauContainer.getChildren().add(plateauPane);

        drawerList.setItems(actions);

        //Quand la liste est appuyée executer l'option
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
    }

    @FXML
    private void handleHamburgerClick() {
        drawer.toggle();
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
