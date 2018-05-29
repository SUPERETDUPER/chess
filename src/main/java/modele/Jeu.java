package modele;

import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Couleur;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Set;

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

    public void commencer() {
        joueurs.get(tourA).getMouvement(this::jouer);
    }

    public void jouer(Move move) {
        move.appliquer(jeuData.getPlateau()); //Jouer
        tourA = tourA == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = jeuData.getAllLegalMoves(tourA);

        if (moves.isEmpty()) {
            if (Helper.roiInCheck(jeuData.getPlateau(), jeuData.getRoi(tourA))) {
                System.out.println("Checkmate");
            } else {
                System.out.println("Stalemate");
            }
        } else {
            joueurs.get(tourA).getMouvement(this::jouer); //Notifier l'autre joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}