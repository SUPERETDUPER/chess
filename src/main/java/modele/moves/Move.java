package modele.moves;

import modele.board.Board;
import modele.board.Position;

import java.util.Objects;

public class Move {
    private final Position start;
    private final Position end;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    void apply(Board board) {
        board.movePiece(start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Move)) return false;
        if (!this.start.equals(((Move) obj).start)) return false;
        return this.end.equals(((Move) obj).end);
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }
}
