package gui;

import gui.view.Case;
import gui.view.PieceDisplay;
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

    private final List<PieceDisplay> pieceDisplays = new ArrayList<>();

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
                    (position.getColonne() + position.getRangee()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    position
            );

            aCase.xProperty().bind(taille.multiply(position.getColonne()));
            aCase.yProperty().bind(taille.multiply(position.getRangee()));

            //Ajouter la case au plateau et à la liste
            plateau.getChildren().add(aCase);
            cases.add(position, aCase);
        }

        positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            //Afficher les pièces
            Piece piece = jeuData.getPlateau().getPiece(position);

            if (piece != null) {
                PieceDisplay pieceDisplay = new PieceDisplay(piece, taille);
                pieceDisplay.setPosition(position);
                pieceDisplay.setOnMousePressed(event -> piecePressed(event, pieceDisplay));
                pieceDisplay.setOnMouseDragged(event -> pieceDragged(event, pieceDisplay));
                pieceDisplay.setOnMouseReleased(event -> pieceDropped(event, pieceDisplay));

                plateau.getChildren().add(pieceDisplay);
                pieceDisplays.add(pieceDisplay);
            }
        }
    }

    public void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.moveRequest = moveRequest;
    }

    private void piecePressed(MouseEvent mouseEvent, PieceDisplay pieceDisplay) {
        //Si aucun moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        Piece pieceClicked = pieceDisplay.getPiece();

        //Si le moveRequest pour l'autre couleur ne rien faire
        if (moveRequest.getCouleurDeLaDemande() != pieceClicked.getCouleur())
            return;

        highlightController.select(jeuData.getPlateau().getPosition(pieceClicked));

        //Calculer les mouvements possibles
        Set<Mouvement> mouvements = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

        //Highlight chaque mouvement
        for (Mouvement mouvement : mouvements) {
            highlightController.addOption(mouvement);
        }

        pieceDisplay.layoutXProperty().unbind();
        pieceDisplay.layoutYProperty().unbind();

//        //Si une pièce est déjà sélectionné
//        if (highlightController.isSelected()) {
//            //Si la case est une des options appliquer le movement
//            if (highlightController.isOption(pieceDisplay)) {
//                moveRequest.apply(highlightController.getMove(pieceDisplay));
//            }
//
//            highlightController.enleverHighlight(); //Déselectionner tout
//        } else {
//
//        }
    }

    private void pieceDragged(MouseEvent mouseEvent, PieceDisplay pieceDisplay) {
        if (highlightController.isSelected()) {
            double positionX = mouseEvent.getX() + (pieceDisplay.getLayoutX());
            double positionY = mouseEvent.getY() + (pieceDisplay.getLayoutY());

            pieceDisplay.relocate(positionX - (pieceDisplay.getWidth() / 2),
                    positionY - (pieceDisplay.getHeight() / 2)
            );

            Position position = getPosition(positionX, positionY);

            if (highlightController.isOption(position)) {
                highlightController.setBordure(position);
            }
        }
    }

    private void pieceDropped(MouseEvent mouseEvent, PieceDisplay pieceDisplay) {
        double positionX = mouseEvent.getX() + (pieceDisplay.getLayoutX());
        double positionY = mouseEvent.getY() + (pieceDisplay.getLayoutY());

        Position position = getPosition(positionX, positionY);

        if (highlightController.isOption(position)) {
            moveRequest.apply(highlightController.getMove(position));
        } else {
            pieceDisplay.setPosition(jeuData.getPlateau().getPosition(pieceDisplay.getPiece()));
        }

        highlightController.enleverHighlight();
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
        PieceDisplay pieceToRemove = null;
        for (PieceDisplay pieceDisplay : pieceDisplays) {
            Position position = jeuData.getPlateau().getPosition(pieceDisplay.getPiece());

            if (position != null) {
                pieceDisplay.setPosition(position);
            } else {
                plateau.getChildren().remove(pieceDisplay);
                pieceToRemove = pieceDisplay;
            }
        }

        if (pieceToRemove != null) pieceDisplays.remove(pieceToRemove);
    }
}