package modele;

import modele.board.Board;
import org.jetbrains.annotations.NotNull;

public class Modele {
    @NotNull
    private final Board board;

    public Modele(@NotNull Board board) {
        this.board = board;
    }

    @NotNull
    public Board getBoard() {
        return board;
    }
}
