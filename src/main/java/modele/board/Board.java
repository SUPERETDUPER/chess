package modele.board;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final BiMap<Position, Piece> board;

    public Board(Map<Position, Piece> board) {
        this.board = HashBiMap.create(board);
    }

    public Board() {
        this(new HashMap<>(32));
    }

    @Nullable
    public Piece getPiece(Position position) {
        return board.get(position);
    }

    @Nullable
    public Position getPosition(Piece piece){
        return board.inverse().get(piece);
    }

    public void movePiece(Position previous, Position next){
        if (previous == next) throw new IllegalArgumentException("Position initiale et finale sont identiques");
        Piece piece = board.remove(previous);
        if (piece == null) throw new IllegalArgumentException("Aucune pièce à: " + previous);
        board.put(next, piece);
    }

    public void ajouter(Position position, Piece piece) {
        board.put(position, piece);
    }

    public void removePiece(Position position){
        if (board.remove(position) == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
    }
}
