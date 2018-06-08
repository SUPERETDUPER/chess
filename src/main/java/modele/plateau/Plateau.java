package modele.plateau;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import modele.Couleur;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represente le plateau de jeu. Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class Plateau implements Serializable {
    private final BiMap<Position, Piece> board;

    public Plateau() {
        this.board = HashBiMap.create(32);
    }

    public Plateau(Map<Position, Piece> startingMap) {
        board = HashBiMap.create(startingMap);
    }

    /**
     * @return vrai si la pièce peut se faire manger par une pièce de l'autre couleur
     */
    public boolean isPieceAttaquer(Piece piece) {
        for (Piece attaqueur : iteratePieces()) {
            if (attaqueur.getCouleur() != piece.getCouleur() && attaqueur.attaquePosition(this, getPosition(piece))) {
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
    public Piece bougerPiece(@NotNull Position position, @NotNull Piece piece) {
        return board.forcePut(position, piece);
    }

    @NotNull
    public Piece removePiece(@NotNull Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
        return remove;
    }

    @NotNull
    public Position removePiece(@NotNull Piece piece) {
        Position position = board.inverse().remove(piece);
        if (position == null) throw new IllegalArgumentException("Aucune pièce à: " + piece);
        return position;
    }

    @NotNull
    public Set<Piece> iteratePieces() {
        return board.values();
    }

    /**
     * @return une nouveau plateau de jeu avec les mêmes pièces et positions
     */
    @NotNull
    public Plateau getCopie() {
        return new Plateau(board);
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
    public Set<Mouvement> getAllMoves(Couleur couleur, JeuData jeuData) {
        Set<Mouvement> mouvements = new HashSet<>();

        for (Piece piece : iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                mouvements.addAll(piece.generateAllMoves(this));
            }
        }
        return mouvements;
    }
}
