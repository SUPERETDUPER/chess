package modele;

import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.Set;

public class Jeu {
    private final JeuData jeuData;

    private Couleur tourA = Couleur.BLANC;

    public Jeu(JeuData jeuData) {
        this.jeuData = jeuData;
    }

    public void commencer() {
        jeuData.getJoueur(tourA).getMouvement(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(jeuData.getPlateau()); //Jouer
        tourA = tourA == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = jeuData.getAllLegalMoves(tourA);

        if (moves.isEmpty()) {
            if (Helper.roiInCheck(jeuData.getPlateau(), jeuData.getRoi(tourA))) {
                System.out.println("Checkmate");
            } else {
                System.out.println("Stalemate");
            }
        } else {
            jeuData.getJoueur(tourA).getMouvement(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}