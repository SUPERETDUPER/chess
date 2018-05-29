package gui;

import modele.moves.Move;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

class HighlightController {
    @NotNull
    private final Tableau<CaseController> caseControllers;

    @NotNull
    private HashMap<Position, Move> currentOptions = new HashMap<>();

    @Nullable
    private Position selectedPosition;

    HighlightController(@NotNull Tableau<CaseController> caseControllers) {
        this.caseControllers = caseControllers;
    }

    void select(@NotNull Position position) {
        this.selectedPosition = position;

        //Surligner la position de départ
        caseControllers.get(position).setCouleur(CaseController.Highlight.ROUGE);
    }

    boolean isSelected() {
        return selectedPosition != null;
    }

    void addOption(Move move) {
        if (selectedPosition != null) {
            Position positionToDisplay = move.getPositionToDisplay();
            currentOptions.put(positionToDisplay, move);
            caseControllers.get(positionToDisplay).setCouleur(CaseController.Highlight.BLUE);
        }
    }

    boolean isOption(Position position) {
        return currentOptions.containsKey(position);
    }

    Move getMove(Position position) {
        return currentOptions.get(position);
    }

    void erase() {
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
