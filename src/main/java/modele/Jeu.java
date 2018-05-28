package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public void setJoueurBlanc(@Nullable Joueur joueurBlanc) {
        this.joueurBlanc = joueurBlanc;
    }

    public void setJoueurNoir(@Nullable Joueur joueurNoir) {
        this.joueurNoir = joueurNoir;
    }

    public void commencer() {
        getCurrentPlayer().notifierTour(new MoveEvent(true, this::jouer));
    }

    public void jouer(Move move) {
        move.apply(board);
        currentPlayerIsWhite = !currentPlayerIsWhite;
        getCurrentPlayer().notifierTour(new MoveEvent(currentPlayerIsWhite, this::jouer));
    }

    private Joueur getCurrentPlayer() {
        return currentPlayerIsWhite ? joueurBlanc : joueurNoir;
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