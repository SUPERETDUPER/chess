package modele.moves;

import modele.board.Board;
import modele.board.Position;

import java.util.Objects;

public abstract class Move {
    final Position start;
    final Position end;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getPositionToDisplay() {
        return end;
    }

    public abstract void apply(Board board);

    public abstract void undo(Board board);

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
