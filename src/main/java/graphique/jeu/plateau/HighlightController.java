package graphique.jeu.plateau;

import graphique.jeu.plateau.element.CasePane;
import modele.mouvement.Mouvement;
import modele.util.Position;
import modele.util.Tableau;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

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
    private final Tableau<CasePane> cases;

    /**
     * La liste de mouvements possibles pour la case sélectionné
     * Ordonné par la position finale
     * Vide si rien n'est sélectionné
     */
    @NotNull
    private final HashMap<Position, Mouvement> mouvementsPossibles = new HashMap<>();

    /**
     * La case présentement sélectionné
     * Null si rien n'est sélectionné
     */
    @Nullable
    private Position selectedPosition;

    /**
     * @param cases la liste de cases
     */
    HighlightController(@NotNull Tableau<CasePane> cases) {
        this.cases = cases;
    }

    /**
     * Sélectionner une case et les positions possibles
     *
     * @param position            la position de la case à sélectionner (en rouge)
     * @param mouvementsPossibles la liste de mouvements possibles
     */
    void selectionner(@NotNull Position position, Set<Mouvement> mouvementsPossibles) {
        this.deSelectionner();

        //Surligner la position de départ
        this.selectedPosition = position;
        cases.get(position).setStyle(CasePane.Style.ROUGE);

        //Surligner toutes les mouvementsPossibles
        for (Mouvement mouvement : mouvementsPossibles) {
            Position positionToDisplay = mouvement.getFin();

            this.mouvementsPossibles.put(positionToDisplay, mouvement); //Ajouter à la liste
            cases.get(positionToDisplay).setStyle(CasePane.Style.BLUE); //Surligner
        }
    }

    /**
     * Enlève tout le highlight et déselectionne la case séléctionnée
     */
    void deSelectionner() {
        if (selectedPosition != null) {
            cases.get(selectedPosition).setStyle(CasePane.Style.NORMAL);

            for (Position position : mouvementsPossibles.keySet()) {
                cases.get(position).setStyle(CasePane.Style.NORMAL);
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
     * @param position la position de fin du mouvement
     * @return le mouvement associé à cette position
     */
    Mouvement getMouvementPossible(Position position) {
        return mouvementsPossibles.get(position);
    }
}
