package modele;

import modele.board.Board;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;

public class Modele {
    @NotNull
    private final Board board;
    @NotNull
    private final Roi roiBlanc;
    @NotNull
    private final Roi roiNoir;

    public Modele(@NotNull Board board, @NotNull Roi roiBlanc, @NotNull Roi roiNoir) {
        this.board = board;
        this.roiBlanc = roiBlanc;
        this.roiNoir = roiNoir;
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