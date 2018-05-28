package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Jeu {
    @NotNull
    private final Board board;
    @NotNull
    private final Roi roiBlanc;
    @NotNull
    private final Roi roiNoir;

    //TODO Change to hash enum
    @Nullable
    private Joueur joueurBlanc;
    @Nullable
    private Joueur joueurNoir;

    private Couleur currentPlayerCouleur = Couleur.BLANC;

    public Jeu(@NotNull Board board, @NotNull Roi roiBlanc, @NotNull Roi roiNoir) {
        this.board = board;
        this.roiBlanc = roiBlanc;
        this.roiNoir = roiNoir;
    }

    public boolean roiInCheck(Couleur couleurDuRoi) {
        for (Piece piece : board.iteratePieces()) {
            if (piece.getCouleur() != couleurDuRoi && piece.attacksPosition(board, board.getPosition(getRoi(couleurDuRoi)))) {
                return false;
            }
        }

        return true;
    }

    @NotNull
    public static Set<Move> getAllLegalMoves(Jeu jeu, Couleur couleur) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : jeu.getBoard().iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                moves.addAll(piece.getLegalMoves(jeu));
            }
        }

        return moves;
    }

    public void ajouterJoueur(@NotNull Joueur joueur) {
        if (joueur.getCouleur() == Couleur.BLANC) joueurBlanc = joueur;
        else joueurNoir = joueur;
    }

    public void commencer() {
        getCurrentPlayer().notifierTour(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(board); //Jouer
        currentPlayerCouleur = currentPlayerCouleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = getAllLegalMoves(this, currentPlayerCouleur);

        if (moves.isEmpty()) {
            if (roiInCheck(currentPlayerCouleur)) {
                System.out.println("Stalemate");
            } else {
                System.out.println("Checkmate");
            }
        }

        getCurrentPlayer().notifierTour(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
    }


    @Contract(pure = true)
    private Joueur getCurrentPlayer() {
        return currentPlayerCouleur == Couleur.BLANC ? joueurBlanc : joueurNoir;
    }

    @NotNull
    public Board getBoard() {
        return board;
    }

    public Roi getRoi(Couleur couleur) {
        return couleur == Couleur.BLANC ? this.roiBlanc : roiNoir;
    }
}