package gui.jeu.board;

import gui.jeu.board.view.Case;
import gui.jeu.board.view.PiecePane;
import gui.jeu.board.view.SquareStackPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Piece;
import modele.plateau.Position;
import modele.plateau.PositionIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controle le plateau de jeu
 */
public class BoardController {
    private static final RowConstraints ROW_CONSTRAINT = new RowConstraints();
    private static final ColumnConstraints COLUMN_CONSTRAINTS = new ColumnConstraints();

    static {
        //Créer les constraintes pour les rangées/colonnes
        ROW_CONSTRAINT.setVgrow(Priority.SOMETIMES);
        ROW_CONSTRAINT.setPercentHeight(100.0F / Position.LIMITE);
        COLUMN_CONSTRAINTS.setHgrow(Priority.SOMETIMES);
        COLUMN_CONSTRAINTS.setPercentWidth(100.0F / Position.LIMITE);
    }

    //La liste de case
    @NotNull
    private final Tableau<Case> cases = new Tableau<>();

    private final List<PiecePane> piecePanes = new ArrayList<>();

    //Le plateau
    @FXML
    private Pane plateau;

    @FXML
    private SquareStackPane plateauContainer;

    //Le modele du jeu (contient le plateau et les pièces)
    @NotNull
    private final JeuData jeuData;

    //Controller pour surligner les cases
    @NotNull
    private final HighlightController highlightController = new HighlightController(cases);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private DemandeDeMouvement moveRequest;

    private AnimationController animationController;

    private GraveyardControllor graveyardControllor;

    public BoardController(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;
    }

    @FXML
    private void initialize() {
        // La taille de chaque case
        DisplayCalculator displayCalculator = new DisplayCalculator(plateau.heightProperty());

        animationController = new AnimationController(displayCalculator);
        graveyardControllor = new GraveyardControllor(displayCalculator, plateau);
        plateauContainer.setDisplayCalculator(displayCalculator);

        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            //Créer un controleur
            Case aCase = new Case(displayCalculator,
                    (position.getColonne() + position.getRangee()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    this::handleClick,
                    position
            );

            //Ajouter la case au plateau et à la liste
            cases.add(position, aCase);
            plateau.getChildren().addAll(aCase);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = jeuData.getPlateau().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, displayCalculator, position);

                //Ajouter les listeners
                piecePane.setOnMousePressed(event -> handleClick(jeuData.getPlateau().getPosition(piecePane.getPiece())));

                //Ajouter la pièce à la liste de pièce
                piecePanes.add(piecePane);
            }
        }

        //Ajouter toutes les cases et pièces au plateau
        plateau.getChildren().addAll(piecePanes);

        this.jeuData.setChangeListener(this::updateBoard);
        updateBoard();
    }

    /**
     * Appelé par un joueur pour demander à l'objet d'enregistrer le mouvement du joueur
     *
     * @param moveRequest l'information sur le mouvement demandé
     */
    public void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.moveRequest = moveRequest;
    }

    private void handleClick(Position position) {
        //Si aucun moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        Piece pieceClicked = jeuData.getPlateau().getPiece(position);

        //Si une pièce est déjà sélectionné
        if (highlightController.isSelected()) {
            //Si la case est une des options appliquer le movement
            if (highlightController.isOption(position)) {
                Mouvement mouvementChoisi = highlightController.getMouvement(position);
                highlightController.deSelectionner();
                moveRequest.apply(mouvementChoisi);
            }

            highlightController.deSelectionner(); //Déselectionner tout
        } else {
            //Quitter si il n'y a rien a faire
            if (pieceClicked == null || moveRequest.getCouleurDeLaDemande() != pieceClicked.getCouleur())
                return;

            //Calculer les mouvements possibles
            Set<Mouvement> moves = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

            highlightController.selectionner(position, moves);
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        Platform.runLater(() -> {
            List<PiecePane> piecesToRemove = new ArrayList<>();
            for (PiecePane piecePane : piecePanes) {
                Position position = jeuData.getPlateau().getPosition(piecePane.getPiece());

                if (position != null) {
                    animationController.addToQueue(piecePane, position);
                } else {
                    plateau.getChildren().remove(piecePane);
                    graveyardControllor.addPiece(piecePane);
                    piecesToRemove.add(piecePane);
                }
            }

            for (PiecePane piecePane : piecesToRemove) {
                piecePanes.remove(piecePane);
            }
        });
    }
}