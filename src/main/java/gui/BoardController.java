package gui;

import gui.view.Case;
import gui.view.PieceDisplay;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
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


    //La liste de controllers de case
    @NotNull
    private final Tableau<Case> cases = new Tableau<>();

    private final List<PieceDisplay> pieceDisplays = new ArrayList<>();

    //Le grid pane qui représente le plateau
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
                PieceDisplay pieceDisplay = new PieceDisplay(piece, taille, piece1 -> caseClicked(jeuData.getPlateau().getPosition(piece1)));
                pieceDisplay.setPosition(position);

                plateau.getChildren().add(pieceDisplay);
                pieceDisplays.add(pieceDisplay);
            }
        }
    }

    public void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.moveRequest = moveRequest;
    }

    private void caseClicked(Position positionClicked) {

        //Si aucun moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        Piece pieceClicked = jeuData.getPlateau().getPiece(positionClicked);

        //Si une pièce est déjà sélectionné
        if (highlightController.isSelected()) {
            //Si la case est une des options appliquer le movement
            if (highlightController.isOption(positionClicked)) {
                moveRequest.apply(highlightController.getMove(positionClicked));
            }

            highlightController.enleverHighlight(); //Déselectionner tout
        } else {
            //Quitter si il n'y a rien a faire
            if (pieceClicked == null || moveRequest.getCouleurDeLaDemande() != pieceClicked.getCouleur())
                return;

            highlightController.select(positionClicked);

            //Calculer les mouvements possibles
            Set<Mouvement> mouvements = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

            //Highlight chaque mouvement
            for (Mouvement mouvement : mouvements) {
                highlightController.addOption(mouvement);
            }
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        for (PieceDisplay pieceDisplay : pieceDisplays) {
//            pieceDisplay.setPosition(jeuData.getPlateau().getPosition(pieceDisplay.getPiece()));
        }
    }
}