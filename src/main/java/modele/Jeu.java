package modele;

import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Jeu {
    private final JeuData jeuData;

    private Couleur tourA = Couleur.BLANC;

    public Jeu(JeuData jeuData) {
        this.jeuData = jeuData;
    }

    public boolean roiInCheck(Couleur couleurDuRoi) {
        for (Piece piece : jeuData.getBoard().iteratePieces()) {
            if (piece.getCouleur() != couleurDuRoi && piece.attacksPosition(jeuData.getBoard(), jeuData.getBoard().getPosition(jeuData.getRoi(couleurDuRoi)))) {
                return false;
            }
        }

        return true;
    }

    @NotNull
    public Set<Move> getAllLegalMoves(Couleur couleur) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : jeuData.getBoard().iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                moves.addAll(piece.getLegalMoves(this));
            }
        }

        return moves;
    }

    public void commencer() {
        jeuData.getJoueur(tourA).notifierTour(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(jeuData.getBoard()); //Jouer
        tourA = tourA == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = getAllLegalMoves(tourA);

        if (moves.isEmpty()) {
            if (roiInCheck(tourA)) {
                System.out.println("Checkmate");
            } else {
                System.out.println("Stalemate");
            }
        }

        jeuData.getJoueur(tourA).notifierTour(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
    }

    public JeuData getJeuData() {
        return jeuData;
    }
}