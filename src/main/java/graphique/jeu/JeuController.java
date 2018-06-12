package graphique.jeu;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import graphique.jeu.plateau.JoueurHumain;
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

/**
 * Controlle l'intreface de jeu
 */
public class JeuController {
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

    private final Chargeur chargeur;

    public JeuController(Runnable goBack, Chargeur chargeur) {
        this.chargeur = chargeur;
        this.plateauPane = new PlateauPane(chargeur.getJeu().getJeuData()); //Créer le plateau pane

        int counter = 0;

        for (Joueur joueur : chargeur.getJeu().getJoueurs().values()) {
            if (joueur instanceof JoueurHumain) {
                ((JoueurHumain) joueur).initializeInterface(plateauPane);
                counter += 1;
            }
        }

        final int joueursHumain = counter;

        //Ajouter un listener pour quand la partie fini
        chargeur.getJeu().setResultatListener(resultat -> Platform.runLater(() -> handleResultat(resultat)));

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
        //Ajouter le plateau
        plateauContainer.getChildren().add(plateauPane);

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

        chargeur.getJeu().notifierProchainJoueur();
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
