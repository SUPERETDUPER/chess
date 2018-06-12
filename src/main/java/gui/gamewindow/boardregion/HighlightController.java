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
 * Contrôle comment les cases devraient être surlignées
 * <p>
 * Il y a toujours une case rouge (la case séléctionnée) avec les mouvements possibles en blue
 */
class HighlightController {
    /**
     * Chaque case
     */
    @NotNull
    private final Board<SquarePane> cases;

    /**
     * La liste de mouvements possibles pour la case sélectionné
     * Ordonné par la position finale
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private final HashMap<Position, Move> mouvementsPossibles = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    /**
     * @param cases la liste de cases
     */
    HighlightController(@NotNull Board<SquarePane> cases) {
        this.cases = cases;
    }

    /**
     * Sélectionner une case et les positions possibles
     *
     * @param position            la position de la case à sélectionner (en rouge)
     * @param mouvementsPossibles la liste de mouvements possibles
     */
    void selectionner(@NotNull Position position, Collection<Move> mouvementsPossibles) {
        this.deSelectionner();

        //Surligner la position de départ
        this.selectedPosition = position;
        cases.get(position).setStyle(SquarePane.Style.ROUGE);

        //Surligner toutes les mouvementsPossibles
        for (Move move : mouvementsPossibles) {
            Position positionToDisplay = move.getFin();

            this.mouvementsPossibles.put(positionToDisplay, move); //Ajouter à la liste
            cases.get(positionToDisplay).setStyle(SquarePane.Style.BLUE); //Surligner
        }
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void deSelectionner() {
        if (selectedPosition != null) {
            cases.get(selectedPosition).setStyle(SquarePane.Style.NORMAL);

            for (Position position : mouvementsPossibles.keySet()) {
                cases.get(position).setStyle(SquarePane.Style.NORMAL);
            }

            mouvementsPossibles.clear();
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
    boolean isMouvementPossible(Position position) {
        return mouvementsPossibles.containsKey(position);
    }

    /**
     * @param position la position de fin du moves
     * @return le moves associé à cette position
     */
    Move getMouvementPossible(Position position) {
        return mouvementsPossibles.get(position);
    }
}
