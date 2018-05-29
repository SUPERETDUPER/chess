package gui;

import modele.moves.Move;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Contrôle comment les cases devraient être surlignées
 */
class HighlightController {
    /**
     * Le controlleur de chaque case
     */
    @NotNull
    private final Tableau<CaseController> caseControllers;

    /**
     * La liste d'option pour la case sélectionné
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private HashMap<Position, Move> currentOptions = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    HighlightController(@NotNull Tableau<CaseController> caseControllers) {
        this.caseControllers = caseControllers;
    }

    /**
     * Sélectionner une case
     *
     * @param position la position de la case
     */
    void select(@NotNull Position position) {
        this.enleverHighlight();
        this.selectedPosition = position;

        //Surligner la position de départ
        caseControllers.get(position).setCouleur(CaseController.Highlight.ROUGE);
    }

    /**
     * @return vrai si une case est séléctionné
     */
    boolean isSelected() {
        return selectedPosition != null;
    }

    /**
     * Ajouter une option (un mouvement possible)
     * @param move le mouvement
     */
    void addOption(Move move) {
        if (selectedPosition != null) {
            Position positionToDisplay = move.getFin();
            currentOptions.put(positionToDisplay, move);
            caseControllers.get(positionToDisplay).setCouleur(CaseController.Highlight.BLUE);
        }
    }

    /**
     *
     * @param position la position à vérifier
     * @return vrai si la position est une option
     */
    boolean isOption(Position position) {
        return currentOptions.containsKey(position);
    }

    /**
     * @param position
     * @return le mouvement associé à cette position
     */
    Move getMove(Position position) {
        return currentOptions.get(position);
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void enleverHighlight() {
        if (selectedPosition != null) {
            for (Position position : currentOptions.keySet()) {
                caseControllers.get(position).setCouleur(CaseController.Highlight.NORMAL);
            }

            caseControllers.get(selectedPosition).setCouleur(CaseController.Highlight.NORMAL);

            currentOptions.clear();
            selectedPosition = null;
        }
    }
}
