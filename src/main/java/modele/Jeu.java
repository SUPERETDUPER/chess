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
    private EnumMap<Couleur, Joueur> joueurs;

    private Couleur tourA = Couleur.BLANC;

    public Jeu(JeuData jeuData, @NotNull EnumMap<Couleur, Joueur> joueurs) {
        this.jeuData = jeuData;
        this.joueurs = joueurs;
    }

    /**
     * Commencer la partie
     */
    public void commencer() {
        joueurs.get(tourA).getMouvement(this::jouer, tourA);
    }

    /**
     * Appelé par le callback de joueur.getMouvement()
     *
     * @param mouvement le mouvement à jouer
     */
    private void jouer(@NotNull Mouvement mouvement) {
        mouvement.appliquer(jeuData.getPlateau()); //Jouer le mouvement

        jeuData.notifyListenerOfChange(jeuData.getPlateau().getCopie());

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
            joueurs.get(tourA).getMouvement(this::jouer, tourA); //Notifier l'autre joueur qu'il peut joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}