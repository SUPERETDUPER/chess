package gui;

import gui.view.Case;
import modele.moves.Mouvement;
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
    private final Tableau<Case> caseControllers;

    /**
     * La liste d'option pour la case sélectionné
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private HashMap<Position, Mouvement> currentOptions = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    @Nullable
    private Position avecBordure;

    HighlightController(@NotNull Tableau<Case> caseControllers) {
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
        caseControllers.get(position).setCouleur(Case.Highlight.ROUGE);
    }

    /**
     * @return vrai si une case est séléctionné
     */
    boolean isSelected() {
        return selectedPosition != null;
    }

    public void setBordure(@Nullable Position position) {
        if (avecBordure != null) {
            caseControllers.get(avecBordure).setCouleur(Case.Highlight.BLUE);
        }

        this.avecBordure = position;
        caseControllers.get(position).setCouleur(Case.Highlight.BLUE_BORDURE);
    }

    /**
     * Ajouter une option (un mouvement possible)
     * @param mouvement le mouvement
     */
    void addOption(Mouvement mouvement) {
        if (selectedPosition != null) {
            Position positionToDisplay = mouvement.getFin();
            currentOptions.put(positionToDisplay, mouvement);
            caseControllers.get(positionToDisplay).setCouleur(Case.Highlight.BLUE);
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
    Mouvement getMove(Position position) {
        return currentOptions.get(position);
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void enleverHighlight() {
        if (selectedPosition != null) {
            for (Position position : currentOptions.keySet()) {
                caseControllers.get(position).setCouleur(Case.Highlight.NORMAL);
            }

            caseControllers.get(selectedPosition).setCouleur(Case.Highlight.NORMAL);

            currentOptions.clear();
            selectedPosition = null;
            avecBordure = null;
        }
    }
}
