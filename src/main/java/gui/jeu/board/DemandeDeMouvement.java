package gui.jeu.board;

import modele.moves.Mouvement;
import modele.pieces.Couleur;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Un objet qui représente une demande de mouvement d'un joueur humain sur le plateau de jeu
 */
public class DemandeDeMouvement {
    /**
     * La méthode à appeler quand le movement est décidé
     */
    @NotNull
    private final Consumer<Mouvement> moveCallback;

    /**
     * la couleur du joueur
     */
    @NotNull
    private final Couleur couleurDeLaDemande;

    /**
     * Si la demande a été complété
     */
    private boolean isConsumed = false;

    public DemandeDeMouvement(@NotNull Consumer<Mouvement> callback, @NotNull Couleur couleurDuTour) {
        this.moveCallback = callback;
        this.couleurDeLaDemande = couleurDuTour;
    }

    void apply(Mouvement mouvement) {
        isConsumed = true;
        moveCallback.accept(mouvement);
    }

    boolean isCompleted() {
        return isConsumed;
    }

    @NotNull
    Couleur getCouleurDeLaDemande() {
        return couleurDeLaDemande;
    }
}
