package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.Modele;
import modele.board.Board;
import modele.board.BoardChangeListener;
import modele.board.Position;
import modele.moves.Move;
import modele.pieces.Piece;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Controle le plateau de jeu
 */
public class BoardController implements BoardChangeListener, CaseClickListener {
    private static final int TAILLE_DU_PLATEAU = 8;

    private final CaseController[][] caseControllers = new CaseController[TAILLE_DU_PLATEAU][TAILLE_DU_PLATEAU];

    @FXML
    private GridPane plateau;

    private Modele modele;

    private final HashMap<Position, Move> highlighted = new HashMap<>();

    public BoardController(Modele modele) {
        this.modele = modele;
    }

    @FXML
    private void initialize() throws IOException {
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        rowConstraint.setPercentHeight(100.0F / TAILLE_DU_PLATEAU);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setPercentWidth(100.0F / TAILLE_DU_PLATEAU);

        //Crée une case pour chaque position
        for (int i = 0; i < TAILLE_DU_PLATEAU; i++) {
            for (int j = 0; j < TAILLE_DU_PLATEAU; j++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/case.fxml"));
                caseControllers[i][j] = new CaseController(
                        new Position(i, j),
                        this,
                        (i + j) % 2 == 0 //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                );

                fxmlLoader.setController(caseControllers[i][j]);

                plateau.add(fxmlLoader.load(), i, j);
            }

            plateau.getRowConstraints().add(rowConstraint);
            plateau.getColumnConstraints().add(columnConstraints);
        }

        this.modele.getBoard().addListener(this);
        notifyChange(this.modele.getBoard());
    }

    @Override
    public void caseClicked(Position position) {
        Piece piece = modele.getBoard().getPiece(position);

        //Si aucun highlight et aucune pièce
        if (highlighted.isEmpty() && piece == null) return;


        //Si aucun highlight et pièce appuyé, surligner toutes les possibilités
        if (highlighted.isEmpty()) {
            Set<Move> moves = piece.generateMoves(modele.getBoard());
            for (Move move : moves) {
                Position endPosition = move.getEnd();
                highlighted.put(endPosition, move);
                caseControllers[endPosition.getIndexRangee()][endPosition.getIndexColonne()].setHighlight(true);
            }
        }
        //Si highlighted et move exist
        else if (highlighted.containsKey(position)) {
            highlighted.get(position).apply(modele.getBoard());
            for (int i = 0; i < TAILLE_DU_PLATEAU; i++) {
                for (int j = 0; j < TAILLE_DU_PLATEAU; j++) {
                    caseControllers[i][j].setHighlight(false);
                }
            }
            highlighted.clear();
        }
        //Si highlighted et move n'existe pas
        else {
            for (int i = 0; i < TAILLE_DU_PLATEAU; i++) {
                for (int j = 0; j < TAILLE_DU_PLATEAU; j++) {
                    caseControllers[i][j].setHighlight(false);
                }
            }
            highlighted.clear();
        }
    }

    @Override
    public void notifyChange(Board board) {
        for (int i = 0; i < TAILLE_DU_PLATEAU; i++) {
            for (int j = 0; j < TAILLE_DU_PLATEAU; j++) {
                caseControllers[i][j].setPiece(board.getPiece(new Position(i, j)));
            }
        }
    }
}