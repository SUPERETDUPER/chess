package graphique.jeu.plateau;

import graphique.jeu.plateau.element.CasePane;
import graphique.jeu.plateau.element.PiecePane;
import graphique.jeu.plateau.placement.GraveyardController;
import graphique.jeu.plateau.placement.PositionCase;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import modele.JeuData;
import modele.mouvement.Mouvement;
import modele.pieces.Piece;
import modele.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Controle la zone du plateau de jeu
 */
public class PlateauPane extends Pane {

    /**
     * La liste de cases
     */
    @NotNull
    private final Tableau<CasePane> cases = new Tableau<>();

    /**
     * La liste de pièces
     */
    @NotNull
    private final List<PiecePane> piecePanes = new ArrayList<>();

    /**
     * La controlleur d'animation
     */
    @NotNull
    private final AnimationController animationController = new AnimationController();

    /**
     * Le util de jeu et les rois
     */
    @NotNull
    private final JeuData jeuData;

    @NotNull
    private final HighlightController highlightController = new HighlightController(cases);

    @NotNull
    private final EnumMap<Couleur, GraveyardController> graveyardControllers = new EnumMap<>(Couleur.class);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private DemandeDeMouvement demandeDeMouvement;

    /**
     * @param jeuData le util de jeu
     */
    public PlateauPane(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;

        //Créer les graveyards et les ajouter à la liste
        GraveyardController graveyardBlanc = new GraveyardController(
                this.heightProperty(),
                false
        );

        this.graveyardControllers.put(Couleur.BLANC, graveyardBlanc);

        this.graveyardControllers.put(
                Couleur.NOIR,
                new GraveyardController(
                        this.heightProperty(),
                        true,
                        this.heightProperty().add(graveyardBlanc.getLargeurTotale())
                )
        );

        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            PositionCase positionGraphique = new PositionCase(position, this.heightProperty(), graveyardBlanc.getLargeurTotale());

            //Créer la case
            CasePane casePane = new CasePane(
                    (position.getColonne() + position.getRangee()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    this::handleClick,
                    positionGraphique //La position de la case
            );

            //Ajouter la case à la liste
            cases.add(position, casePane);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = jeuData.getPlateau().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, positionGraphique);

                //Ajouter les listeners
                piecePane.setOnMousePressed(event -> handleClick(jeuData.getPlateau().getPosition(piece)));

                //Ajouter la pièce à la liste de pièce
                piecePanes.add(piecePane);
            }
        }

        //Ajouter toutes pièces au util
        this.getChildren().addAll(cases.getValues());
        this.getChildren().addAll(piecePanes);

        this.jeuData.setChangeListener(this::replacerLesPieces); // Si il y a un changement replacer les pièces
    }

    /**
     * Appelé par un joueur pour demander au util d'enregistrer le mouvement du joueur humain
     *
     * @param moveRequest l'information sur le mouvement demandé
     */
    void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.demandeDeMouvement = moveRequest;
    }

    /**
     * Quand une position est appuyé
     *
     * @param position la position est appuyé
     */
    private void handleClick(Position position) {
        //Si aucune demandeDeMouvement ne rien faire
        if (demandeDeMouvement == null || demandeDeMouvement.isCompleted()) return;

        //Obtenir la pièce à cette position
        Piece pieceClicked = jeuData.getPlateau().getPiece(position);

        //Si rien n'est sélectionné
        if (!highlightController.isSelected()) {
            //Si la pièce existe et elle est de la couleur du demandeDeMouvement
            if (pieceClicked != null && demandeDeMouvement.getCouleur() == pieceClicked.getCouleur()) {

                //Calculer les mouvements possibles
                List<Mouvement> moves = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

                highlightController.selectionner(position, moves); //Sélectionner
            }
        } else {
            //Si la case est une des options appliquer le movement
            if (highlightController.isMouvementPossible(position)) {
                Mouvement mouvementChoisi = highlightController.getMouvementPossible(position);
                highlightController.deSelectionner();
                demandeDeMouvement.apply(mouvementChoisi);
            } else {
                highlightController.deSelectionner(); //Déselectionner tout
            }
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void replacerLesPieces(Plateau plateau) {
        //Sur le FX Thread
        Platform.runLater(() -> {
            //Pour chaque pieces déplacer à position
            for (int i = 0; i < piecePanes.size(); i++) {
                PiecePane piecePane = piecePanes.get(i);

                if (piecePane != null) {
                    piecePane.setText();
                    Position position = plateau.getPosition(piecePane.getPiece());

                    //Si la position existe déplacer à position
                    //Si la position n'existe pas déplacer à graveyard
                    if (position == null) {
                        animationController.ajouterAnimation(
                                piecePane,
                                graveyardControllers.get(piecePane.getPiece().getCouleur()).getNextGraveyardPosition()
                        );

                        piecePanes.set(i, null);
                    } else {
                        animationController.ajouterAnimation(
                                piecePane,
                                new PositionCase(position, this.heightProperty(), graveyardControllers.get(Couleur.BLANC).getLargeurTotale())
                        );
                    }
                }
            }
        });
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré comme étant dépendant de la hauteur
     */
    @Override
    protected double computePrefWidth(double height) {
        double ratio = 1;

        for (GraveyardController graveyardController : graveyardControllers.values()) {
            ratio += graveyardController.getTotalWidthRatio();
        }

        return height * ratio;
    }
}