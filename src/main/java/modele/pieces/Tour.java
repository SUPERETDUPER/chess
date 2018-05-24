package modele.pieces;

import modele.board.Board;
import modele.board.Position;

public class Tour extends RepeatOffsetPiece {
    private static final int[][] OFFSET = {
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };

    public Tour(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean attacksPosition(Board board, Position position) {
        Position currentPosition = board.getPosition(this);
        if (position.getIndexColonne() != currentPosition.getIndexColonne() && position.getIndexRangee() != currentPosition.getIndexRangee())
            return false;

        return super.attacksPosition(board, position);
    }

    @Override
    int unicodeForWhite() {
        return 9814;
    }

    @Override
    int unicodeForBlack() {
        return 9820;
    }

    @Override
    int[][] getRepeatOffset() {
        return OFFSET;
    }
}
