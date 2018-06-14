package model.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import model.pieces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * Represents a mapping of pieces and their position on the board.
 * Uses {@link BiMap} from Google Guava to allow quick access in both directions (find position of piece or find piece at position)
 */
//TODO review usage of synchronised
public class BoardMap implements Serializable {
    @NotNull
    private final BiMap<Position, Piece> board;

    public BoardMap() {
        this.board = HashBiMap.create(32);
    }

    /**
     * @return a new board map with the initial (starting) layout of pieces
     */
    @NotNull
    public static BoardMap createStartingBoard() {
        BoardMap boardMap = new BoardMap();
        boardMap.add(new Position(7, 0), new Rook(Colour.WHITE));
        boardMap.add(new Position(7, 1), new Knight(Colour.WHITE));
        boardMap.add(new Position(7, 2), new Bishop(Colour.WHITE));
        boardMap.add(new Position(7, 3), new Queen(Colour.WHITE));
        boardMap.add(new Position(7, 4), new King(Colour.WHITE));
        boardMap.add(new Position(7, 5), new Bishop(Colour.WHITE));
        boardMap.add(new Position(7, 6), new Knight(Colour.WHITE));
        boardMap.add(new Position(7, 7), new Rook(Colour.WHITE));

        boardMap.add(new Position(6, 0), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 1), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 2), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 3), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 4), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 5), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 6), new Pawn(Colour.WHITE));
        boardMap.add(new Position(6, 7), new Pawn(Colour.WHITE));

        boardMap.add(new Position(1, 0), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 1), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 2), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 3), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 4), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 5), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 6), new Pawn(Colour.BLACK));
        boardMap.add(new Position(1, 7), new Pawn(Colour.BLACK));

        boardMap.add(new Position(0, 0), new Rook(Colour.BLACK));
        boardMap.add(new Position(0, 1), new Knight(Colour.BLACK));
        boardMap.add(new Position(0, 2), new Bishop(Colour.BLACK));
        boardMap.add(new Position(0, 3), new Queen(Colour.BLACK));
        boardMap.add(new Position(0, 4), new King(Colour.BLACK));
        boardMap.add(new Position(0, 5), new Bishop(Colour.BLACK));
        boardMap.add(new Position(0, 6), new Knight(Colour.BLACK));
        boardMap.add(new Position(0, 7), new Rook(Colour.BLACK));

        return boardMap;
    }


    @Nullable
    public Piece getPiece(Position position) {
        return board.get(position);
    }

    @Nullable
    public Position getPosition(@NotNull Piece piece) {
        return board.inverse().get(piece);
    }

    /**
     * @return the piece that was at this position before the new piece was added
     */
    @Nullable
    public Piece add(@NotNull Position position, @NotNull Piece piece) {
        return board.put(position, piece);
    }

    /**
     * @param piece the piece that was at the destination before the piece was moved
     */
    public synchronized void movePiece(@NotNull Position destination, @NotNull Piece piece) {
        board.forcePut(destination, piece);
    }

    @NotNull
    public synchronized Piece removePiece(@NotNull Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("No piece at: " + position);
        return remove;
    }

    @NotNull
    public synchronized Position removePiece(@NotNull Piece piece) {
        Position position = board.inverse().remove(piece);
        if (position == null) throw new IllegalArgumentException("Piece not on the board: piece:" + piece);
        return position;
    }

    @NotNull
    public Set<Piece> iteratePieces() {
        return board.values();
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (Piece piece : iteratePieces()) {
            stringBuilder.append(getPosition(piece)).append(" ").append(piece).append(", ");
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }


    //Do not delete contains synchronized keyword
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
