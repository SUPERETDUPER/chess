package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;

public class Modele {
    @FunctionalInterface
    public interface MoveCallback {
        void jouer(Move move);
    }

    @NotNull
    private final Board board;
    @NotNull
    private final Roi roiBlanc;
    @NotNull
    private final Roi roiNoir;
    @NotNull
    private final Joueur joueurBlanc;
    @NotNull
    private final Joueur joueurNoir;

    public Modele(@NotNull Board board, @NotNull Roi roiBlanc, @NotNull Roi roiNoir, @NotNull Joueur joueurBlanc, @NotNull Joueur joueurNoir) {
        this.board = board;
        this.roiBlanc = roiBlanc;
        this.roiNoir = roiNoir;
        this.joueurBlanc = joueurBlanc;
        this.joueurNoir = joueurNoir;
    }

    public void commencer() {
        joueurBlanc.notifierTour(this::jouer);
    }

    public void jouer(Move move) {
        move.apply(board);
        joueurNoir.notifierTour(this::jouer);
    }

    @NotNull
    public Board getBoard() {
        return board;
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