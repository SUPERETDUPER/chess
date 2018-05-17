package main.modele.board;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Board {
    private final int[][] board = new int[8][8];
    private final Piece[] pieces = new Piece[32];

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = -1;
            }
        }

        //TODO Setup pieces
    }

    @Nullable
    public Piece getPiece(int rangee, int colonne) {
        int id = board[rangee][colonne];
        if (id == -1) return null;
        return pieces[id];
    }

    public void setPosition(@NotNull Position position, int id) {
        Piece piece = pieces[id];
        Position positionOld = piece.getPosition();
        board[positionOld.getIndexRangee()][positionOld.getIndexColonne()] = -1;
        if (board[position.getIndexRangee()][position.getIndexColonne()] != -1)
            throw new RuntimeException("Piece déjà sur cette case");
        board[position.getIndexRangee()][position.getIndexColonne()] = id;
        piece.setPosition(position);
    }

    public void removePiece(@NotNull Position position) {
        pieces[board[position.getIndexRangee()][position.getIndexColonne()]].setPosition(null);
        board[position.getIndexRangee()][position.getIndexRangee()] = -1;
    }
}
