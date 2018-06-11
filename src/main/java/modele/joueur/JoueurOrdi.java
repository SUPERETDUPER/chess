package modele.joueur;

import modele.JeuData;
import modele.mouvement.Mouvement;
import modele.util.Couleur;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird
//TODO consider moving pieces values (ex. pawn = 1) to this class since pieces should not be responsible for the algorithm

/**
 * Un joueur qui utilise un algorithm pour trouver le prochain meilleur mouvement.
 * L'algorithme est un algorithm recursif min-max qui utilise les valeurs des pièces (chaque pièce à une valeur)
 *
 * Il existe deux niveaux de difficulté (facile et difficile)
 */
public class JoueurOrdi extends Joueur {
    //Les niveaux de difficultés
    public static final Difficulte NIVEAU_FACILE = new Difficulte(3, "Facile");
    public static final Difficulte NIVEAU_DIFFICILE = new Difficulte(4, "Difficile");

    /**
     * Le niveau de difficulte du joueur
     */
    private final Difficulte difficulte;

    /**
     * Le jeu data
     */
    @Nullable
    private JeuData jeuData;

    /**
     * @param difficulte la difficulté de l'algorithme
     */
    public JoueurOrdi(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    @Override
    public void initializeJeuData(@NotNull JeuData jeuData) {
        this.jeuData = jeuData;
    }

    /**
     * Retourne le mouvement qui retourne le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        new Thread(() ->
                callback.accept(
                        calculerMeilleurMouvement(new MoveSequence(), couleur)
                                .getPremierMouvement()
                )
        ).start();
    }

    @NotNull
    private MoveSequence calculerMeilleurMouvement(MoveSequence pastSequence, Couleur couleur) {
        //Si on est déjà à la profondeur maximale retourner
        if (pastSequence.getLongeur() == difficulte.profondeur) return pastSequence;

        //Calculer les mouvements possibles
        List<Mouvement> mouvementsPossibles = jeuData.getAllLegalMoves(couleur);

        MoveSequence meilleurMouvement = null;

        for (Mouvement mouvement : mouvementsPossibles) {
            mouvement.appliquer(jeuData); //Appliquer le mouvement

            //Calculer la valeur du mouvement (partie récursive)
            MoveSequence moveSequence = calculerMeilleurMouvement(new MoveSequence(pastSequence, mouvement), inverser(couleur));

            //Si le mouvement est meilleur que meilleurMouvement remplacer meilleurMouvement
            //Si le mouvement à la même valeur, remplacer 50% du temps
            if (meilleurMouvement == null) {
                meilleurMouvement = moveSequence;
            } else {
                if (couleur == Couleur.BLANC) {
                    if (moveSequence.getValeur() > meilleurMouvement.getValeur()
                            || (moveSequence.getValeur() == meilleurMouvement.getValeur() && Math.random() > 0.5)) {
                        meilleurMouvement = moveSequence;
                    }
                } else {
                    if (moveSequence.getValeur() < meilleurMouvement.getValeur()
                            || (moveSequence.getValeur() == meilleurMouvement.getValeur() && Math.random() > 0.5)) {
                        meilleurMouvement = moveSequence;
                    }
                }
            }

            mouvement.undo(jeuData); //Défaire les changements
        }

        return meilleurMouvement == null ? pastSequence : meilleurMouvement;
    }

    /**
     * Si blanc retourne noir et vice-versa
     *
     * @param couleur la couleur à inverser
     * @return la couleur inversée
     */
    private Couleur inverser(Couleur couleur) {
        return couleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC;
    }

    /**
     * Une classe qui représente une série de mouvements et la valeur de la série
     */
    private class MoveSequence {
        private final List<Mouvement> mouvements;

        /**
         * La somme de la valeur de toutes les pièces
         */
        private final int valeur;

        MoveSequence() {
            this.valeur = 0;
            this.mouvements = new ArrayList<>();
        }

        private MoveSequence(MoveSequence moveSequence, Mouvement mouvement) {
            this.mouvements = new ArrayList<>(moveSequence.mouvements);
            this.mouvements.add(mouvement);
            this.valeur = moveSequence.valeur + mouvement.getValeur();
        }

        int getValeur() {
            return valeur;
        }

        int getLongeur() {
            return mouvements.size();
        }

        Mouvement getPremierMouvement() {
            return mouvements.get(0);
        }
    }

    /**
     * La difficulté de l'algorithm (dépendant de la profondeur)
     */
    private static class Difficulte implements Serializable {
        private final int profondeur;
        private final String nom;

        Difficulte(int profondeur, String nom) {
            this.profondeur = profondeur;
            this.nom = nom;
        }
    }

    @Override
    public String getNom() {
        return "Ordinateur (" + difficulte.nom + ")";
    }
}