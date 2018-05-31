package gui;

import gui.view.Case;
import gui.view.PiecePane;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    private StackPane plateauContainer;

    //Le modele du jeu (contient le plateau et les pièces)
    @NotNull
    private final JeuData jeuData;

    //Controller pour surligner les cases
    @NotNull
    private final HighlightController highlightController = new HighlightController(cases);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private DemandeDeMouvement moveRequest;

    /**
     * La taille de chaque case
     */
    private NumberBinding taille;

    BoardController(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;
        this.jeuData.setNewChangeListener(this::updateBoard);
    }

    @FXML
    private void initialize() {
        taille = Bindings.divide(plateau.heightProperty(), Position.LIMITE);

        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            //Créer un controleur
            Case aCase = new Case(taille,
                    (position.getColonne() + position.getRangee()) % 2 == 0 //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
            );

            aCase.xProperty().bind(taille.multiply(position.getColonne()));
            aCase.yProperty().bind(taille.multiply(position.getRangee()));
            aCase.setOnMouseClicked(event -> highlightController.deSelectionner());

            //Ajouter la case au plateau et à la liste
            cases.add(position, aCase);
            plateau.getChildren().addAll(aCase);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = jeuData.getPlateau().getPiece(position);

            if (piece != null) {
                PiecePane piecePaneDisplay = new PiecePane(piece, taille);
                placerPiece(piecePaneDisplay, position);

                //Ajouter les listeners
                piecePaneDisplay.setOnMousePressed(event -> piecePressed(event, piecePaneDisplay));
                piecePaneDisplay.setOnMouseDragged(event -> pieceDragged(event, piecePaneDisplay));
                piecePaneDisplay.setOnMouseReleased(event -> pieceDropped(event, piecePaneDisplay));

                //Ajouter la pièce à la liste de pièce
                piecePanes.add(piecePaneDisplay);
            }
        }

        //Ajouter toutes les cases et pièces au plateau
        plateau.getChildren().addAll(piecePanes);
    }

    /**
     * Appelé par un joueur pour demander à l'objet d'enregistrer le mouvement du joueur
     *
     * @param moveRequest l'information sur le mouvement demandé
     */
    public void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.moveRequest = moveRequest;
    }

    private void piecePressed(MouseEvent mouseEvent, PiecePane piecePane) {
        //Si aucun moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        Piece pieceClicked = piecePane.getPiece();

        //Si le moveRequest pour l'autre couleur ne rien faire
        if (moveRequest.getCouleurDeLaDemande() != pieceClicked.getCouleur())
            return;

        //Calculer les mouvements possibles
        Set<Mouvement> mouvements = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

        //Surligner les options et la pièce
        highlightController.selectionner(jeuData.getPlateau().getPosition(pieceClicked), mouvements);

        //Permettre la pièce de se déplacer
        piecePane.layoutXProperty().unbind();
        piecePane.layoutYProperty().unbind();
    }

    private void pieceDragged(MouseEvent mouseEvent, PiecePane piecePane) {
        //Si la pièce est entrain d'être dragged
        if (!piecePane.layoutXProperty().isBound()) {
            //Obtenir sa position selon la souris
            double positionX = mouseEvent.getX() + (piecePane.getLayoutX());
            double positionY = mouseEvent.getY() + (piecePane.getLayoutY());

            //Déplacer à cette position
            double offset = taille.getValue().doubleValue() / 2.0;

            piecePane.relocate(
                    positionX - offset,
                    positionY - offset
            );

            //Surligner la case approprié
            Position position = getPosition(positionX, positionY);

            if (highlightController.isOption(position)) {
                highlightController.setBordure(position);
            } else {
                highlightController.enleverBordure();
            }
        }
    }

    private void pieceDropped(@NotNull MouseEvent mouseEvent, @NotNull PiecePane piecePane) {
        double positionX = mouseEvent.getX() + (piecePane.getLayoutX());
        double positionY = mouseEvent.getY() + (piecePane.getLayoutY());

        Position position = getPosition(positionX, positionY);

        if (highlightController.isOption(position)) {
            Mouvement mouvement = highlightController.getMouvement(position);
            highlightController.deSelectionner();
            moveRequest.apply(mouvement);
        } else {
            placerPiece(piecePane, jeuData.getPlateau().getPosition(piecePane.getPiece()));
        }
    }

    private Position getPosition(double x, double y) {
        int colonne = Position.LIMITE - 1;
        int rangee = Position.LIMITE - 1;

        for (int i = 0; i < Position.LIMITE; i++) {
            if (cases.get(new Position(0, i)).getX() > x) {
                colonne = i - 1;
                break;
            }
        }

        for (int i = 0; i < Position.LIMITE; i++) {
            if (cases.get(new Position(i, 0)).getY() > y) {
                rangee = i - 1;
                break;
            }
        }

        return new Position(rangee, colonne);
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        PiecePane piecePaneToRemove = null;
        for (PiecePane piecePane : piecePanes) {
            Position position = jeuData.getPlateau().getPosition(piecePane.getPiece());

            if (position != null) {
                placerPiece(piecePane, position);
            } else {
                plateau.getChildren().remove(piecePane);
                piecePaneToRemove = piecePane;
            }
        }

        if (piecePaneToRemove != null) piecePanes.remove(piecePaneToRemove);
    }

    /**
     * Place la pièce à la position
     */
    private void placerPiece(PiecePane piecePane, Position position) {
        piecePane.layoutXProperty().bind(taille.multiply(position.getColonne()));
        piecePane.layoutYProperty().bind(taille.multiply(position.getRangee()));
    }
}