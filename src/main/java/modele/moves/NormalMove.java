package modele.moves;

import modele.board.Board;
import modele.board.Position;

import java.util.Objects;

public class NormalMove implements Move {
    private final Position start;
    private final Position end;

    public NormalMove(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public void apply(Board board) {
        board.movePiece(start, end);
        board.notifyChange();
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public Position getPositionToDisplay() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof NormalMove)) return false;
        if (!this.start.equals(((NormalMove) obj).start)) return false;
        return this.end.equals(((NormalMove) obj).end);
    }

    @Override
    public String toString() {
        return "From: " + start + " to: " + end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }
}
