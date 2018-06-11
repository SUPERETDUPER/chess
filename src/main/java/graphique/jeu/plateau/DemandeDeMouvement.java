package graphique.jeu.plateau;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Un objet qui représente une demande de mouvement d'un joueur humain sur le util de jeu
 * Utilisé pour demander au PlateauPane de laisser le joueur humain effectuer un mouvement et capturer le résultat
 */
class DemandeDeMouvement {
    /**
     * La méthode à appeler quand le movement est décidé
     */
    @NotNull
    private final Consumer<Mouvement> moveCallback;

    /**
     * la couleur du joueur
     */
    @NotNull
    private final Couleur couleur;

    /**
     * Si la demande a été complété
     */
    private boolean isCompleted = false;

    DemandeDeMouvement(@NotNull Consumer<Mouvement> callback, @NotNull Couleur couleur) {
        this.moveCallback = callback;
        this.couleur = couleur;
    }

    void apply(Mouvement mouvement) {
        isCompleted = true;
        moveCallback.accept(mouvement);
    }

    boolean isCompleted() {
        return isCompleted;
    }

    @NotNull
    Couleur getCouleur() {
        return couleur;
    }
}
