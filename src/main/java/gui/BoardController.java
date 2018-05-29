package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.JeuData;
import modele.MoveCallbackWrapper;
import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.plateau.Position;
import modele.plateau.PositionIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
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
        ROW_CONSTRAINT.setPercentHeight(100.0F / Position.getLimite());
        COLUMN_CONSTRAINTS.setHgrow(Priority.SOMETIMES);
        COLUMN_CONSTRAINTS.setPercentWidth(100.0F / Position.getLimite());
    }


    //La liste de controllers de case
    @NotNull
    private final Tableau<CaseController> caseControllers = new Tableau<>();

    @FXML
    private GridPane plateau;

    @NotNull
    private final JeuData jeuData;

    @Nullable
    private HashMap<Position, Move> currentMoves = null;

    @Nullable
    private MoveCallbackWrapper moveCallbackWrapper;
    private Couleur couleurDuTour;

    public BoardController(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;
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
        for (int i = 0; i < Position.getLimite(); i++) {
            plateau.getRowConstraints().add(ROW_CONSTRAINT);
            plateau.getColumnConstraints().add(COLUMN_CONSTRAINTS);
        }

        updateBoard(); //Afficher les pièces tels quelles le sont présentement
    }

    public void getMovement(Couleur couleurDuTour, MoveCallbackWrapper moveCallbackWrapper) {
        this.moveCallbackWrapper = moveCallbackWrapper;
        this.couleurDuTour = couleurDuTour;
    }

    private void caseClicked(Position position) {
        Piece piece = jeuData.getPlateau().getPiece(position);

        //Si aucun pièce pré-sélectionné
        if (currentMoves == null) {
            //Quitter si il n'y a rien a faire
            if (piece == null || moveCallbackWrapper == null || moveCallbackWrapper.isConsumed() || couleurDuTour != piece.getCouleur())
                return;

            //Calculer les mouvements possibles
            Set<Move> moves = piece.getLegalMoves(jeuData);

            currentMoves = new HashMap<>();

            //Ajouter le mouvement à la liste
            for (Move move : moves) {
                addCurrentMove(move);
            }

            //Surligner la position de départ
            caseControllers.get(position).setCouleur(CaseController.Highlight.ROUGE);

        } else {
            //Si la case est une des options appliquer le movement
            if (currentMoves.containsKey(position)) {
                moveCallbackWrapper.jouer(currentMoves.get(position));
                updateBoard(); //Affiche changement
            }

            removeCurrentMoves(); //Déselectionner tout
        }
    }

    /**
     * Ajoute un movement à la liste de mouvements possible et surligne cette case
     *
     * @param move le movement à montrer
     */
    private void addCurrentMove(Move move) {
        Position positionToDisplay = move.getPositionToDisplay();
        currentMoves.put(positionToDisplay, move);
        caseControllers.get(positionToDisplay).setCouleur(CaseController.Highlight.BLUE);
    }

    /**
     * Enlève tout les mouvements
     */
    private void removeCurrentMoves() {
        for (CaseController caseController : caseControllers) {
            caseController.setCouleur(CaseController.Highlight.NORMAL);
        }

        currentMoves = null;
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            caseControllers.get(position).setPiece(jeuData.getPlateau().getPiece(position));
        }
    }
}