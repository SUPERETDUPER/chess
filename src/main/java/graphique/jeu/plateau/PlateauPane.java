package graphique.jeu.plateau;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import graphique.jeu.plateau.element.CasePane;
import graphique.jeu.plateau.element.PiecePane;
import graphique.jeu.plateau.placement.GraveyardController;
import graphique.jeu.plateau.placement.PositionCase;
import graphique.jeu.plateau.placement.PositionGraphique;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import modele.Jeu;
import modele.JeuData;
import modele.mouvement.Mouvement;
import modele.pieces.Piece;
import modele.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumMap;

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
    private final BiMap<Piece, PiecePane> piecePanes = HashBiMap.create(32);

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
     * @param jeu le modele
     */
    public PlateauPane(@NotNull Jeu jeu) {
        this.jeuData = jeu.getJeuData();

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
                piecePane.setOnMousePressed(event -> handleClick(piecePane.getCurrentPosition()));

                //Ajouter la pièce à la liste de pièce
                piecePanes.put(piece, piecePane);
            }
        }

        for (Piece piece : jeuData.getEatenPieces()) {
            PiecePane piecePane = new PiecePane(piece, graveyardControllers.get(piece.getCouleur()).getNextGraveyardPosition());
            piecePane.setOnMouseClicked(event -> this.handleClick(piecePane.getCurrentPosition()));

            piecePanes.put(piece, piecePane);
        }

        //Ajouter toutes pièces au util
        this.getChildren().addAll(cases.getValues());
        this.getChildren().addAll(piecePanes.values());

        this.jeuData.setChangeListener(this::replacerLesPieces); // Si il y a un changement replacer les pièces

        jeu.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Jeu.Status.INACTIF) {
                if (animationController.getAnimationEnCours().get())
                    animationController.setOnFinishListener(jeu::notifierProchainJoueur);
                else jeu.notifierProchainJoueur();
            }
        });
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
     * @param positionGraphique la position est appuyé
     */
    private void handleClick(PositionGraphique positionGraphique) {
        if (!(positionGraphique instanceof PositionCase)) return;

        Position position = ((PositionCase) positionGraphique).getPosition();

        //Si aucune demandeDeMouvement ne rien faire
        if (demandeDeMouvement == null || demandeDeMouvement.isCompleted()) return;

        //Obtenir la pièce à cette position
        Piece pieceClicked = jeuData.getPlateau().getPiece(position);

        //Si rien n'est sélectionné
        if (!highlightController.isSelected()) {
            //Si la pièce existe et elle est de la couleur du demandeDeMouvement
            if (pieceClicked != null && demandeDeMouvement.getCouleur() == pieceClicked.getCouleur()) {

                //Calculer les mouvements possibles
                Collection<Mouvement> moves = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau(), position), pieceClicked.getCouleur());

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
    private synchronized void replacerLesPieces(Plateau plateau) {
//        while (animationController.getAnimationEnCours().get()) Thread.yield();

        for (Piece piece : plateau.iteratePieces()) {
            Position position = plateau.getPosition(piece);

            PositionCase positionGraphique = new PositionCase(position, this.heightProperty(), graveyardControllers.get(Couleur.BLANC).getLargeurTotale());
            PiecePane piecePane = piecePanes.get(piece);

            if (!piecePane.isAtPosition(positionGraphique)) {
                animationController.ajouterAnimation(piecePane,
                        positionGraphique
                );
            }
        }

        for (Piece piece : jeuData.getEatenPieces()) {
            PiecePane piecePane = piecePanes.get(piece);
            if (!graveyardControllers.get(piece.getCouleur()).isInGraveyard(piece)) {
                animationController.ajouterAnimation(
                        piecePane,
                        graveyardControllers.get(piece.getCouleur()).getNextGraveyardPosition()
                );
            }
        }
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