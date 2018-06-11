package graphique.jeu.plateau;

import modele.JeuData;
import modele.joueur.Joueur;
import modele.mouvement.Mouvement;
import modele.util.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur qui utilise le util de jeu pour soumettre ses mouvements
 */
public class JoueurHumain extends Joueur {
    transient private PlateauPane plateauPane;

    @Override
    public void initializeJeuData(JeuData jeuData) {

    }

    public void initializeInterface(PlateauPane plateauPane) {
        this.plateauPane = plateauPane;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le util de jeu
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        plateauPane.demanderMouvement(new DemandeDeMouvement(callback, couleur));
    }

    @Override
    public String getNom() {
        return "Humain";
    }
}
