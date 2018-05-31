package gui;

import gui.view.Case;
import modele.moves.Mouvement;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

/**
 * Contrôle comment les cases devraient être surlignées
 */
class HighlightController {
    /**
     * Le controlleur de chaque case
     */
    @NotNull
    private final Tableau<Case> cases;

    /**
     * La liste d'option pour la case sélectionné
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private HashMap<Position, Mouvement> options = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    @Nullable
    private Position caseAvecBordure;

    HighlightController(@NotNull Tableau<Case> cases) {
        this.cases = cases;
    }

    /**
     * Sélectionner une case et les positions possibles
     *
     * @param position la position de la case
     * @param options les options de mouvement possible
     */
    void selectionner(@NotNull Position position, Set<Mouvement> options) {
        this.deSelectionner();

        //Surligner la position de départ
        this.selectedPosition = position;
        cases.get(position).setStyle(Case.Style.ROUGE);

        //Surligner toutes les options
        for (Mouvement mouvement : options) {
            Position positionToDisplay = mouvement.getFin();

            this.options.put(positionToDisplay, mouvement);
            cases.get(positionToDisplay).setStyle(Case.Style.BLUE);
        }
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void deSelectionner() {
        if (selectedPosition != null) {
            cases.get(selectedPosition).setStyle(Case.Style.NORMAL);

            for (Position position : options.keySet()) {
                cases.get(position).setStyle(Case.Style.NORMAL);
            }

            options.clear();
            selectedPosition = null;
            caseAvecBordure = null;
        }
    }

    void setBordure(@NotNull Position position) {
        if (caseAvecBordure != position) {
            enleverBordure();

            this.caseAvecBordure = position;
            cases.get(position).setStyle(Case.Style.BLUE_BORDURE);
        }
    }

    void enleverBordure() {
        if (caseAvecBordure != null) {
            cases.get(caseAvecBordure).setStyle(Case.Style.BLUE);
        }
    }

    /**
     * @param position la position à vérifier
     * @return vrai si la position est une option
     */
    boolean isOption(Position position) {
        return options.containsKey(position);
    }

    /**
     * @param position la position de fin du mouvement
     * @return le mouvement associé à cette position
     */
    Mouvement getMouvement(Position position) {
        return options.get(position);
    }
}
