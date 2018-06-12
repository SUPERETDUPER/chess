package gui.gamewindow.boardregion;

import gui.gamewindow.boardregion.components.SquarePane;
import model.moves.Move;
import model.util.Board;
import model.util.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

/**
 * Contrôle comment les squares devraient être surlignées
 * <p>
 * Il y a toujours une case rouge (la case séléctionnée) avec les mouvements possibles en blue
 */
//TODO Use positions instead of moves
class HighlightController {
    /**
     * Chaque case
     */
    @NotNull
    private final Board<SquarePane> squares;

    /**
     * La liste de mouvements possibles pour la case sélectionné
     * Ordonné par la position finale
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private final HashMap<Position, Move> possibleMoves = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    /**
     * @param squares la liste de squares
     */
    HighlightController(@NotNull Board<SquarePane> squares) {
        this.squares = squares;
    }

    /**
     * Sélectionner une case et les positions possibles
     *
     * @param position            la position de la case à sélectionner (en rouge)
     * @param possibleMoves la liste de mouvements possibles
     */
    void select(@NotNull Position position, Collection<Move> possibleMoves) {
        this.eraseSelection();

        //Surligner la position de départ
        this.selectedPosition = position;
        squares.get(position).setStyle(SquarePane.Style.RED);

        //Surligner toutes les possibleMoves
        for (Move move : possibleMoves) {
            Position positionToDisplay = move.getEnd();

            this.possibleMoves.put(positionToDisplay, move); //Ajouter à la liste
            squares.get(positionToDisplay).setStyle(SquarePane.Style.BLUE); //Surligner
        }
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void eraseSelection() {
        if (selectedPosition != null) {
            squares.get(selectedPosition).setStyle(SquarePane.Style.NORMAL);

            for (Position position : possibleMoves.keySet()) {
                squares.get(position).setStyle(SquarePane.Style.NORMAL);
            }

            possibleMoves.clear();
            selectedPosition = null;
        }
    }

    boolean isSelected() {
        return selectedPosition != null;
    }

    /**
     * @param position la position à vérifier
     * @return vrai si la position est une option
     */
    boolean isPossibleMove(Position position) {
        return possibleMoves.containsKey(position);
    }

    /**
     * @param position la position de fin du moves
     * @return le moves associé à cette position
     */
    Move getPossibleMove(Position position) {
        return possibleMoves.get(position);
    }
}
