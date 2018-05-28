package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import modele.Jeu;
import modele.MoveCallbackWrapper;
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
    //La liste de cases
    @NotNull
    private final CaseController[][] caseControllers = new CaseController[Position.getLimite()][Position.getLimite()];

    @FXML
    private GridPane plateau;

    @NotNull
    private final Jeu jeu;

    @NotNull
    private final HashMap<Position, Move> currentMoves = new HashMap<>();

    @Nullable
    private MoveCallbackWrapper moveCallbackWrapper;

    public BoardController(@NotNull Jeu jeu) {
        this.jeu = jeu;
    }

    @FXML
    private void initialize() throws IOException {
        //Créer les constraintes pour les rangées/colonnes
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        rowConstraint.setPercentHeight(100.0F / Position.getLimite());
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setPercentWidth(100.0F / Position.getLimite());

        //Crée une case pour chaque position
        for (int i = 0; i < Position.getLimite(); i++) {
            for (int j = 0; j < Position.getLimite(); j++) {

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
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        this.moveCallbackWrapper = moveCallbackWrapper;
    }

    private void caseClicked(Position position) {
        Piece piece = jeu.getBoard().getPiece(position);

        //Si aucun pièce pré-sélectionné
        if (currentMoves.isEmpty()) {
            //Quitter si il n'y a rien a faire
            if (piece == null || moveCallbackWrapper == null || moveCallbackWrapper.isConsumed() || moveCallbackWrapper.isWhite() != piece.isWhite())
                return;

            removeCurrentMoves();

            //Calculer les mouvements possibles
            Set<Move> moves = piece.generateLegalMoves(jeu.getBoard(), piece.isWhite() ? jeu.getRoiBlanc() : jeu.getRoiNoir());

            //Ajouter le mouvement à la liste
            for (Move move : moves) {
                addCurrentMove(move);
            }

            //Surligner la position de départ
            caseControllers[position.getRangee()][position.getColonne()].setCouleur(CaseController.Highlight.ROUGE);

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
        caseControllers[positionToDisplay.getRangee()][positionToDisplay.getColonne()].setCouleur(CaseController.Highlight.BLUE);
    }

    /**
     * Enlève tout les mouvements
     */
    private void removeCurrentMoves() {
        for (int i = 0; i < Position.getLimite(); i++) {
            for (int j = 0; j < Position.getLimite(); j++) {
                caseControllers[i][j].setCouleur(CaseController.Highlight.NORMAL);
            }
        }

        currentMoves.clear();
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void updateBoard() {
        for (int i = 0; i < Position.getLimite(); i++) {
            for (int j = 0; j < Position.getLimite(); j++) {
                caseControllers[i][j].setPiece(jeu.getBoard().getPiece(new Position(i, j)));
            }
        }
    }
}