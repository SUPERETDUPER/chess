package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class Jeu {
    @NotNull
    private final Board board;
    @NotNull
    private final Roi roiBlanc;
    @NotNull
    private final Roi roiNoir;

    @Nullable
    private Joueur joueurBlanc;
    @Nullable
    private Joueur joueurNoir;

    private boolean currentPlayerIsWhite = true;

    public Jeu(@NotNull Board board, @NotNull Roi roiBlanc, @NotNull Roi roiNoir) {
        this.board = board;
        this.roiBlanc = roiBlanc;
        this.roiNoir = roiNoir;
    }

    public void ajouterJoueur(@NotNull Joueur joueur) {
        if (joueur.isBlanc()) joueurBlanc = joueur;
        else joueurNoir = joueur;
    }

    public void commencer() {
        getCurrentPlayer().notifierTour(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(board); //Jouer
        currentPlayerIsWhite = !currentPlayerIsWhite; //Changer le tour

        Set<Move> moves = Helper.getAllLegalMoves(currentPlayerIsWhite, this.board, this.getRoi(currentPlayerIsWhite));

        if (moves.isEmpty()) {
            if (Helper.boardIsLegal(board, getRoi(currentPlayerIsWhite))) {
                System.out.println("Stalemate");
            } else {
                System.out.println("Checkmate");
            }
        }

        getCurrentPlayer().notifierTour(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
    }


    private Joueur getCurrentPlayer() {
        return currentPlayerIsWhite ? joueurBlanc : joueurNoir;
    }

    @NotNull
    public Board getBoard() {
        return board;
    }

    public Roi getRoi(boolean isRoiBlanc) {
        return isRoiBlanc ? this.roiBlanc : roiNoir;
    }

    @NotNull
    public Roi getRoiBlanc() {
        return roiBlanc;
    }

    @NotNull
    public Roi getRoiNoir() {
        return roiNoir;
    }
}