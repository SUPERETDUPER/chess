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
        this(new HashMap<Position, Piece>(32));

        //TODO Setup pieces
    }

    @Nullable
    public Piece getPiece(Position position) {
        return board.get(position);
    }

    public Position getPosition(Piece piece){
        return board.inverse().get(piece);
    }

    public void movePiece(Position previous, Position next){
        Piece piece = board.remove(previous);
        if (piece == null) throw new IllegalArgumentException("Aucune pièce à: " + previous);
        board.put(next, piece);
    }

    public void put(Piece piece, Position position) {
        board.put(position, piece);
    }

    public void removePiece(Position position){
        board.remove(position);
    }
}
