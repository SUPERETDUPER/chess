package modele.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import modele.mouvement.Mouvement;
import modele.pieces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represente le plateau de jeu.
 * Chaque pièce est attaché à une position
 * Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class Plateau implements Serializable {
    private final BiMap<Position, Piece> board;

    public Plateau() {
        this.board = HashBiMap.create(32);
    }

    private Plateau(Map<Position, Piece> startingMap) {
        board = HashBiMap.create(startingMap);
    }

    /**
     * Retourne un nouveau util avec la position du début
     *
     * @param roiNoir  le roi noir à utiliser
     * @param roiBlanc le roi blanc à utiliser
     * @return le util en position initiale
     */
    @NotNull
    public static Plateau creePlateauDebut(Roi roiNoir, Roi roiBlanc) {
        Plateau plateau = new Plateau();
        plateau.ajouter(new Position(7, 0), new Tour(Couleur.BLANC));
        plateau.ajouter(new Position(7, 1), new Cavalier(Couleur.BLANC));
        plateau.ajouter(new Position(7, 2), new Fou(Couleur.BLANC));
        plateau.ajouter(new Position(7, 3), new Reine(Couleur.BLANC));
        plateau.ajouter(new Position(7, 4), roiBlanc);
        plateau.ajouter(new Position(7, 5), new Fou(Couleur.BLANC));
        plateau.ajouter(new Position(7, 6), new Cavalier(Couleur.BLANC));
        plateau.ajouter(new Position(7, 7), new Tour(Couleur.BLANC));

        plateau.ajouter(new Position(6, 0), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 1), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 2), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 3), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 4), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 5), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 6), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 7), new Pion(Couleur.BLANC));

        plateau.ajouter(new Position(1, 0), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 1), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 2), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 3), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 4), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 5), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 6), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 7), new Pion(Couleur.NOIR));

        plateau.ajouter(new Position(0, 0), new Tour(Couleur.NOIR));
        plateau.ajouter(new Position(0, 1), new Cavalier(Couleur.NOIR));
        plateau.ajouter(new Position(0, 2), new Fou(Couleur.NOIR));
        plateau.ajouter(new Position(0, 3), new Reine(Couleur.NOIR));
        plateau.ajouter(new Position(0, 4), roiNoir);
        plateau.ajouter(new Position(0, 5), new Fou(Couleur.NOIR));
        plateau.ajouter(new Position(0, 6), new Cavalier(Couleur.NOIR));
        plateau.ajouter(new Position(0, 7), new Tour(Couleur.NOIR));

        return plateau;
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
     * @return une nouveau util de jeu avec les mêmes pièces et positions
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
    public Set<Mouvement> getAllMoves(Couleur couleur) {
        Set<Mouvement> mouvements = new HashSet<>();

        for (Piece piece : iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                mouvements.addAll(piece.generateAllMoves(this));
            }
        }
        return mouvements;
    }

    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
