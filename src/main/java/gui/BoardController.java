package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.Modele;
import modele.board.Position;
import modele.moves.Move;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Controle le plateau de jeu
 */
public class BoardController {

    private final CaseController[][] caseControllers = new CaseController[Position.getMax()][Position.getMax()];

    @FXML
    private GridPane plateau;

    @NotNull
    private final Modele modele;

    private final HashMap<Position, Move> currentMoves = new HashMap<>();

    public BoardController(@NotNull Modele modele) {
        this.modele = modele;
    }

    @FXML
    private void initialize() throws IOException {
        //Créer les constraintes pour les rangées/colonnes
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        rowConstraint.setPercentHeight(100.0F / Position.getMax());
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setPercentWidth(100.0F / Position.getMax());

        //Crée une case pour chaque position
        for (int i = 0; i < Position.getMax(); i++) {
            for (int j = 0; j < Position.getMax(); j++) {

                //Créer un controleur
                caseControllers[i][j] = new CaseController(
                        new Position(i, j),
                        this::caseClicked,
                        (i + j) % 2 == 0 //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                );

                //Créer une case avec le controlleur
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/case.fxml"));
                fxmlLoader.setController(caseControllers[i][j]);

                //Ajouter la case
                plateau.add(fxmlLoader.load(), i, j);
            }

            //Appliquer la constraintes
            plateau.getRowConstraints().add(rowConstraint);
            plateau.getColumnConstraints().add(columnConstraints);
        }

        updateBoard();
    }

    private void caseClicked(Position position) {
        Piece piece = modele.getBoard().getPiece(position);

        //Si aucun highlight et aucune pièce
        if (currentMoves.isEmpty() && piece == null) return;


        //Si aucun highlight et pièce appuyé, surligner toutes les possibilités
        if (currentMoves.isEmpty()) {
            Set<Move> moves = piece.generateMoves(modele.getBoard());
            for (Move move : moves) {
                Position displayPosition = move.getPositionToDisplay();
                currentMoves.put(displayPosition, move);
                caseControllers[displayPosition.getIndexRangee()][displayPosition.getIndexColonne()].setHighlight(true);
            }
        }
        //Si currentMoves et move exist
        else if (currentMoves.containsKey(position)) {
            currentMoves.get(position).apply(modele.getBoard()); //Bouge la pièce
            updateBoard(); //Affiche changement
            clearHighlight(); //Enlève le highlight
        }
        //Si highlighted exist et move n'existe pas
        else {
            clearHighlight();
        }
    }

    private void clearHighlight() {
        for (int i = 0; i < Position.getMax(); i++) {
            for (int j = 0; j < Position.getMax(); j++) {
                caseControllers[i][j].setHighlight(false);
            }
        }

        currentMoves.clear();
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        for (int i = 0; i < Position.getMax(); i++) {
            for (int j = 0; j < Position.getMax(); j++) {
                caseControllers[i][j].setPiece(modele.getBoard().getPiece(new Position(i, j)));
            }
        }
    }
}