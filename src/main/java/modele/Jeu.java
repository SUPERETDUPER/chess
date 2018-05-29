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
        jeuData.getJoueur(tourA).notifierTour(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(jeuData.getBoard()); //Jouer
        tourA = tourA == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = jeuData.getAllLegalMoves(tourA);

        if (moves.isEmpty()) {
            if (jeuData.roiInCheck(tourA)) {
                System.out.println("Checkmate");
            } else {
                System.out.println("Stalemate");
            }
        } else {
            jeuData.getJoueur(tourA).notifierTour(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}