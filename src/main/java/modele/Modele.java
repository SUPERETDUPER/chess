package modele;

import modele.board.Board;

public class Modele {
    private final Board board;

    public Modele(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}
