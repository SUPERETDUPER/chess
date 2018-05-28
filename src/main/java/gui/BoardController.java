package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.Jeu;
import modele.MoveEvent;
import modele.board.Position;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Controle le plateau de jeu
 */
public class BoardController implements Joueur {

    private final CaseController[][] caseControllers = new CaseController[Position.getMax()][Position.getMax()];

    @FXML
    private GridPane plateau;

    @NotNull
    private final Jeu jeu;

    @NotNull
    private final HashMap<Position, Move> currentMoves = new HashMap<>();

    @Nullable
    private MoveEvent moveEvent;

    public BoardController(@NotNull Jeu jeu) {
        this.jeu = jeu;
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
                plateau.add(fxmlLoader.load(), j, i);
            }

            //Appliquer la constraintes
            plateau.getRowConstraints().add(rowConstraint);
            plateau.getColumnConstraints().add(columnConstraints);
        }

        updateBoard();
    }

    @Override
    public void notifierTour(MoveEvent moveEvent) {
        this.moveEvent = moveEvent;
    }

    private void caseClicked(Position position) {
        Piece piece = jeu.getBoard().getPiece(position);

        //Si aucun highlight et aucune pièce
        if (currentMoves.isEmpty() && piece == null) return;


        //Si aucun highlight et pièce appuyé, surligner toutes les possibilités
        if (currentMoves.isEmpty()) {
            if (moveEvent != null && moveEvent.isWhite() == piece.isWhite() && !moveEvent.isConsumed()) {
                Set<Move> moves = piece.generateLegalMoves(jeu.getBoard(), piece.isWhite() ? jeu.getRoiBlanc() : jeu.getRoiNoir());
                for (Move move : moves) {
                    Position displayPosition = move.getPositionToDisplay();
                    currentMoves.put(displayPosition, move);
                    caseControllers[displayPosition.getIndexRangee()][displayPosition.getIndexColonne()].setHighlight(CaseController.Highlight.BLUE);
                }

                if (!moves.isEmpty()) {
                    caseControllers[position.getIndexRangee()][position.getIndexColonne()].setHighlight(CaseController.Highlight.ROUGE);
                }
            }
        }

        //Si currentMoves et move exist
        else if (currentMoves.containsKey(position)) {
            //Bouge la pièce
            moveEvent.jouer(currentMoves.get(position));

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
                caseControllers[i][j].setHighlight(CaseController.Highlight.NORMAL);
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
                caseControllers[i][j].setPiece(jeu.getBoard().getPiece(new Position(i, j)));
            }
        }
    }
}