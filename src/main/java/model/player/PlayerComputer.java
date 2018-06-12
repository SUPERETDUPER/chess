package model.player;

import model.GameData;
import model.moves.Move;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird
//TODO consider moving pieces values (ex. pawn = 1) to this class since pieces should not be responsible for the algorithm

/**
 * Un player qui utilise un algorithm pour trouver le prochain meilleur moves.
 * L'algorithme est un algorithm recursif min-max qui utilise les valeurs des pièces (chaque pièce à une valeur)
 *
 * Il existe deux niveaux de difficulté (facile et difficile)
 */
public class PlayerComputer extends Player {
    //Les niveaux de difficultés
    public static final Difficulte NIVEAU_FACILE = new Difficulte(3, "Facile");
    public static final Difficulte NIVEAU_DIFFICILE = new Difficulte(4, "Difficile");

    /**
     * Le niveau de difficulte du player
     */
    private final Difficulte difficulte;

    /**
     * Le gamewindow data
     */
    @Nullable
    private GameData gameData;

    /**
     * @param difficulte la difficulté de l'algorithme
     */
    public PlayerComputer(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    @Override
    public void initializeJeuData(@NotNull GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Retourne le moves qui retourne le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain moves
     */
    @Override
    public void getMouvement(Consumer<Move> callback, Colour colour) {
        new Thread(() ->
                callback.accept(
                        calculerMeilleurMouvement(new MoveSequence(), colour)
                                .getPremierMouvement()
                )
        ).start();
    }

    @NotNull
    private MoveSequence calculerMeilleurMouvement(MoveSequence pastSequence, Colour colour) {
        //Si on est déjà à la profondeur maximale retourner
        if (pastSequence.getLongeur() == difficulte.profondeur) return pastSequence;

        //Calculer les moves possibles
        Collection<Move> mouvementsPossibles = gameData.getAllLegalMoves(colour);

        MoveSequence meilleurMouvement = null;

        for (Move move : mouvementsPossibles) {
            move.appliquer(gameData); //Appliquer le moves

            //Calculer la valeur du moves (partie récursive)
            MoveSequence moveSequence = calculerMeilleurMouvement(new MoveSequence(pastSequence, move), inverser(colour));

            //Si le moves est meilleur que meilleurMouvement remplacer meilleurMouvement
            //Si le moves à la même valeur, remplacer 50% du temps
            if (meilleurMouvement == null) {
                meilleurMouvement = moveSequence;
            } else {
                if (colour == Colour.BLANC) {
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

            move.undo(gameData); //Défaire les changements
        }

        return meilleurMouvement == null ? pastSequence : meilleurMouvement;
    }

    /**
     * Si blanc retourne noir et vice-versa
     *
     * @param colour la colour à inverser
     * @return la colour inversée
     */
    private Colour inverser(Colour colour) {
        return colour == Colour.BLANC ? Colour.NOIR : Colour.BLANC;
    }

    /**
     * Une classe qui représente une série de moves et la valeur de la série
     */
    private class MoveSequence {
        private final List<Move> moves;

        /**
         * La somme de la valeur de toutes les pièces
         */
        private final int valeur;

        MoveSequence() {
            this.valeur = 0;
            this.moves = new ArrayList<>();
        }

        private MoveSequence(MoveSequence moveSequence, Move move) {
            this.moves = new ArrayList<>(moveSequence.moves);
            this.moves.add(move);
            this.valeur = moveSequence.valeur + move.getValeur();
        }

        int getValeur() {
            return valeur;
        }

        int getLongeur() {
            return moves.size();
        }

        Move getPremierMouvement() {
            return moves.get(0);
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