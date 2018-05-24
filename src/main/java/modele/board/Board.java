package modele.board;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public void ajouter(Position position, Piece piece) {
        board.put(position, piece);
    }

    @NotNull
    public Piece removePiece(Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
        return remove;
    }

    public Set<Piece> iteratePieces() {
        return board.values();
    }
}
