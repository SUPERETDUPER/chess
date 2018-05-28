package modele.board;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Represente le plateau de jeu. Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class Board {
    private final BiMap<Position, Piece> board = HashBiMap.create(32);

    @Nullable
    public Piece getPiece(Position position) {
        return board.get(position);
    }

    @Nullable
    public Position getPosition(@NotNull Piece piece) {
        return board.inverse().get(piece);
    }

    public void ajouter(@NotNull Position position, @NotNull Piece piece) {
        board.put(position, piece);
    }

    @NotNull
    public Piece removePiece(@NotNull Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
        return remove;
    }

    @NotNull
    public Set<Piece> iteratePieces() {
        return board.values();
    }
}
