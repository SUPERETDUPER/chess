package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Piece;
import modele.plateau.Position;
import modele.plateau.PositionIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
    private final Tableau<CaseController> caseControllers = new Tableau<>();

    //Le grid pane qui représente le plateau
    @FXML
    private GridPane plateau;

    //Le modele du jeu (contient le plateau et les pièces)
    @NotNull
    private final JeuData jeuData;

    //Controller pour surligner les cases
    @NotNull
    private final HighlightController highlightController = new HighlightController(caseControllers);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private DemandeDeMouvement moveRequest;

    BoardController(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;
        this.jeuData.setNewChangeListener(this::updateBoard);
    }

    @FXML
    private void initialize() throws IOException {
        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            //Créer un controleur
            CaseController caseController = new CaseController(
                    position,
                    this::caseClicked,
                    (position.getColonne() + position.getRangee()) % 2 == 0 //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
            );

            //Créer une case avec le controlleur
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/case.fxml"));
            fxmlLoader.setController(caseController);

            //Ajouter la case au plateau et à la liste
            plateau.add(fxmlLoader.load(), position.getColonne(), position.getRangee());
            caseControllers.add(position, caseController);
        }

        //Ajouter les constraintes pour chaque rangée/colonne
        for (int i = 0; i < Position.LIMITE; i++) {
            plateau.getRowConstraints().add(ROW_CONSTRAINT);
            plateau.getColumnConstraints().add(COLUMN_CONSTRAINTS);
        }

        updateBoard(); //Afficher les pièces tels quelles le sont présentement
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
        for (CaseController caseController : caseControllers) {
            caseController.setPiece(jeuData.getPlateau().getPiece(caseController.getPosition()));
        }
    }
}