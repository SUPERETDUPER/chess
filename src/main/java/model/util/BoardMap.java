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
import java.util.Map;
import java.util.Set;

/**
 * Represente le boardregion de gamewindow.
 * Chaque pièce est attaché à une position
 * Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class BoardMap implements Serializable {
    private final BiMap<Position, Piece> board;

    public BoardMap() {
        this.board = HashBiMap.create(32);
    }

    private BoardMap(Map<Position, Piece> startingMap) {
        board = HashBiMap.create(startingMap);
    }

    /**
     * Retourne un nouveau util avec la position du début
     *
     * @return le util en position initiale
     */
    @NotNull
    public static BoardMap creePlateauDebut() {
        BoardMap boardMap = new BoardMap();
        boardMap.ajouter(new Position(7, 0), new Rook(Colour.BLANC));
        boardMap.ajouter(new Position(7, 1), new Knight(Colour.BLANC));
        boardMap.ajouter(new Position(7, 2), new Bishop(Colour.BLANC));
        boardMap.ajouter(new Position(7, 3), new Queen(Colour.BLANC));
        boardMap.ajouter(new Position(7, 4), new King(Colour.BLANC));
        boardMap.ajouter(new Position(7, 5), new Bishop(Colour.BLANC));
        boardMap.ajouter(new Position(7, 6), new Knight(Colour.BLANC));
        boardMap.ajouter(new Position(7, 7), new Rook(Colour.BLANC));

        boardMap.ajouter(new Position(6, 0), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 1), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 2), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 3), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 4), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 5), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 6), new Pawn(Colour.BLANC));
        boardMap.ajouter(new Position(6, 7), new Pawn(Colour.BLANC));

        boardMap.ajouter(new Position(1, 0), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 1), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 2), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 3), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 4), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 5), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 6), new Pawn(Colour.NOIR));
        boardMap.ajouter(new Position(1, 7), new Pawn(Colour.NOIR));

        boardMap.ajouter(new Position(0, 0), new Rook(Colour.NOIR));
        boardMap.ajouter(new Position(0, 1), new Knight(Colour.NOIR));
        boardMap.ajouter(new Position(0, 2), new Bishop(Colour.NOIR));
        boardMap.ajouter(new Position(0, 3), new Queen(Colour.NOIR));
        boardMap.ajouter(new Position(0, 4), new King(Colour.NOIR));
        boardMap.ajouter(new Position(0, 5), new Bishop(Colour.NOIR));
        boardMap.ajouter(new Position(0, 6), new Knight(Colour.NOIR));
        boardMap.ajouter(new Position(0, 7), new Rook(Colour.NOIR));

        return boardMap;
    }

    /**
     * @return vrai si la pièce peut se faire manger par une pièce de l'autre couleur
     */
    public boolean isPieceAttaquer(Piece piece) {
        for (Piece attaqueur : iteratePieces()) {
            if (attaqueur.getColour() != piece.getColour() && attaqueur.attaquePosition(this, getPosition(piece))) {
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
    public Piece ajouter(@NotNull Position position, @NotNull Piece piece) {
        return board.put(position, piece);
    }

    /**
     * @param piece la pièce qui était à position
     */
    public synchronized void bougerPiece(@NotNull Position position, @NotNull Piece piece) {
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

    /**
     * @return une nouveau util de gamewindow avec les mêmes pièces et positions
     */
    @NotNull
    public BoardMap getCopie() {
        return new BoardMap(board);
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
    public Set<Move> getAllMoves(Colour colour) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : iteratePieces()) {
            if (piece.getColour() == colour) {
                moves.addAll(piece.generateAllMoves(this, this.getPosition(piece)));
            }
        }
        return moves;
    }

    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}