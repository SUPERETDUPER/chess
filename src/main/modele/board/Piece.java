package main.modele.board;

public abstract class Piece {
    private Position position;
    private final boolean isWhite;

    public Piece(Position position, boolean isWhite) {
        this.position = position;
        this.isWhite = isWhite;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
