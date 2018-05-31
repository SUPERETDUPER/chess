package modele;

import modele.joueur.Joueur;
import modele.moves.Mouvement;
import modele.pieces.Couleur;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Set;

/**
 * Classe qui supervise les joueurs et s'assure de respecter les tours
 */
public class Jeu {
    private final JeuData jeuData;

    @NotNull
    private final EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);

    private Couleur tourA = Couleur.BLANC;

    public Jeu(JeuData jeuData) {
        this.jeuData = jeuData;
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.put(joueur.getCouleur(), joueur);
    }

    /**
     * Commencer la partie
     */
    public void commencer() {
        joueurs.get(tourA).getMouvement(this::jouer);
    }

    /**
     * Appelé par le callback de joueur.getMouvement()
     *
     * @param mouvement le mouvement à jouer
     */
    private void jouer(@NotNull Mouvement mouvement) {
        mouvement.appliquer(jeuData.getPlateau()); //Jouer le mouvement

        jeuData.notifyListenerOfChange(mouvement);

        tourA = tourA == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        //Vérifier pour échec et mat ou match nul
        Set<Mouvement> mouvements = jeuData.getAllLegalMoves(tourA);

        if (mouvements.isEmpty()) {
            if (Helper.isPieceAttaquer(jeuData.getPlateau(), jeuData.getRoi(tourA))) {
                System.out.println("Checkmate");
            } else {
                System.out.println("Stalemate");
            }
        } else {
            joueurs.get(tourA).getMouvement(this::jouer); //Notifier l'autre joueur qu'il peut joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}