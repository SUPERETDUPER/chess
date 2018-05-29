package modele.joueur;

import modele.JeuData;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.Set;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max

/**
 * Un joueur qui utilise un algorithm pour trouver son prochain mouvement
 */
public class JoueurOrdi extends Joueur {
    private JeuData jeuData;

    public JoueurOrdi(JeuData jeuData, Couleur couleur) {
        super(couleur);
        this.jeuData = jeuData;
    }

    /**
     * Trouve tous les mouvement possible puis retourne celui qui remporte le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Move> callback) {
        Set<Move> moves = jeuData.getAllLegalMoves(this.getCouleur());

        Move meilleurMouvement = null;

        //Analyse chaque mouvement
        for (Move move : moves) {
            if (meilleurMouvement == null) {
                meilleurMouvement = move;
                continue;
            }

            //Si le mouvement remporte plus de point c'est le nouveau meilleur mouvement
            if (this.getCouleur() == Couleur.BLANC) {
                if (move.getValeur() > meilleurMouvement.getValeur()) {
                    meilleurMouvement = move;
                }
            } else {
                if (move.getValeur() < meilleurMouvement.getValeur()) {
                    meilleurMouvement = move;
                }
            }
        }

        //Appliquer (jouer) le meilleurMouvement
        callback.accept(meilleurMouvement);
    }
}