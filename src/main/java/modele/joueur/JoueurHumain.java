package modele.joueur;

import gui.jeu.board.DemandeDeMouvement;
import gui.jeu.board.PlateauPane;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

/**
 * Un joueur qui utilise le plateau de jeu pour soumettre ses mouvements
 */
public class JoueurHumain extends Joueur {
    private PlateauPane plateauPane;

    @Override
    public void initializeJeuData(JeuData jeuData) {

    }

    @Override
    public void initializeInterface(PlateauPane plateauPane) {
        this.plateauPane = plateauPane;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le plateau de jeu
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        plateauPane.demanderMouvement(new DemandeDeMouvement(callback, couleur));
    }

    @Override
    String getNom() {
        return "Humain";
    }

    private void writeObject(@NotNull ObjectOutputStream out) throws IOException {
        PlateauPane plateauPane = this.plateauPane;
        this.plateauPane = null;
        out.defaultWriteObject();
        this.plateauPane = plateauPane;
    }
}
