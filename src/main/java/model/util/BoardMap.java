package model.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import model.moves.Move;
import model.pieces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represente le boardregion de game.
 * Chaque pièce est attaché à une position
 * Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class BoardMap implements Serializable {
    private final BiMap<Position, Piece> board;

    public BoardMap() {
        this.board = HashBiMap.create(32);
    }

    /**
     * Retourne un nouveau util avec la position du début
     *
     * @return le util en position initiale
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

    /**
     * @return vrai si la pièce peut se faire manger par une pièce de l'autre couleur
     */
    public boolean isPieceAttacked(Piece piece) {
        for (Piece attacker : iteratePieces()) {
            if (attacker.getColour() != piece.getColour() && attacker.isAttackingPosition(this, getPosition(piece))) {
                return true;
            }
        }

        return false;
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
     * @return la pièce qui était à position
     */
    public Piece add(@NotNull Position position, @NotNull Piece piece) {
        return board.put(position, piece);
    }

    /**
     * @param piece la pièce qui était à position
     */
    public synchronized void movePiece(@NotNull Position position, @NotNull Piece piece) {
        board.forcePut(position, piece);
    }

    @NotNull
    public synchronized Piece removePiece(@NotNull Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
        return remove;
    }

    @NotNull
    public synchronized Position removePiece(@NotNull Piece piece) {
        Position position = board.inverse().remove(piece);
        if (position == null) throw new IllegalArgumentException("Aucune pièce à: " + piece);
        return position;
    }

    @NotNull
    public Set<Piece> iteratePieces() {
        return board.values();
    }

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

    @NotNull
    public Set<Move> getAllPossibleMoves(Colour colour) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : iteratePieces()) {
            if (piece.getColour() == colour) {
                moves.addAll(piece.generateAllMoves(this, this.getPosition(piece)));
            }
        }
        return moves;
    }

    //Do not delete contains synchronized keyword
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
