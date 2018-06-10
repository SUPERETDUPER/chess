package modele.plateau;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import modele.Couleur;
import modele.moves.Mouvement;
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
 * Represente le plateau de jeu. Permet d'acceder directement à la pièce à une position ou à la position d'une pièce
 * Utilise le BiMap de Google Guava
 */
public class PlateauPiece implements Serializable, Plateau {
    private BiMap<Position, Piece> board;

    public PlateauPiece() {
        this.board = HashBiMap.create(32);
    }

    public PlateauPiece(Map<Position, Piece> startingMap) {
        board = HashBiMap.create(startingMap);
    }

    /**
     * Retourne un nouveau plateau avec la position du début
     *
     * @param roiNoir  le roi noir à utiliser
     * @param roiBlanc le roi blanc à utiliser
     * @return le plateau en position initiale
     */
    @NotNull
    public static PlateauPiece creePlateauDebut(Roi roiNoir, Roi roiBlanc) {
        PlateauPiece plateau = new PlateauPiece();
        plateau.ajouterPiece(new Tour(Couleur.BLANC), new PositionBase(7, 0));
        plateau.ajouterPiece(new Cavalier(Couleur.BLANC), new PositionBase(7, 1));
        plateau.ajouterPiece(new Fou(Couleur.BLANC), new PositionBase(7, 2));
        plateau.ajouterPiece(new Reine(Couleur.BLANC), new PositionBase(7, 3));
        plateau.ajouterPiece(roiBlanc, new PositionBase(7, 4));
        plateau.ajouterPiece(new Fou(Couleur.BLANC), new PositionBase(7, 5));
        plateau.ajouterPiece(new Cavalier(Couleur.BLANC), new PositionBase(7, 6));
        plateau.ajouterPiece(new Tour(Couleur.BLANC), new PositionBase(7, 7));

        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 0));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 1));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 2));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 3));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 4));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 5));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 6));
        plateau.ajouterPiece(new Pion(Couleur.BLANC), new PositionBase(6, 7));

        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 0));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 1));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 2));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 3));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 4));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 5));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 6));
        plateau.ajouterPiece(new Pion(Couleur.NOIR), new PositionBase(1, 7));

        plateau.ajouterPiece(new Tour(Couleur.NOIR), new PositionBase(0, 0));
        plateau.ajouterPiece(new Cavalier(Couleur.NOIR), new PositionBase(0, 1));
        plateau.ajouterPiece(new Fou(Couleur.NOIR), new PositionBase(0, 2));
        plateau.ajouterPiece(new Reine(Couleur.NOIR), new PositionBase(0, 3));
        plateau.ajouterPiece(roiNoir, new PositionBase(0, 4));
        plateau.ajouterPiece(new Fou(Couleur.NOIR), new PositionBase(0, 5));
        plateau.ajouterPiece(new Cavalier(Couleur.NOIR), new PositionBase(0, 6));
        plateau.ajouterPiece(new Tour(Couleur.NOIR), new PositionBase(0, 7));

        return plateau;
    }

    @Override
    public synchronized void ajouterPiece(Piece piece, Position position) {
        board.put(position, piece);
    }

    @Override
    public Piece bougerPiece(Piece piece, Position finale) {
        return board.forcePut(finale, piece);
    }

    @Override
    public Position mangerPiece(Piece piece) {
        return eneleverPiece(piece);
    }

    @Override
    public Piece mangerPiece(Position position) {
        return eneleverPiece(position);
    }

    @Override
    public synchronized Position eneleverPiece(Piece piece) {
        Position position = board.inverse().remove(piece);
        if (position == null) throw new IllegalArgumentException("Aucune pièce à: " + piece);
        return position;
    }

    @Override
    public synchronized Piece eneleverPiece(Position position) {
        Piece remove = board.remove(position);
        if (remove == null) throw new IllegalArgumentException("Aucune pièce à: " + position);
        return remove;
    }

    @Nullable
    public Position getPosition(@NotNull Piece piece) {
        return board.inverse().get(piece);
    }

    /**
     * @return vrai si la pièce peut se faire manger par une pièce de l'autre couleur
     */
    public boolean isPieceAttaquer(Piece piece) {
        for (Piece attaqueur : board.values()) {
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

    /**
     * @return une nouveau plateau de jeu avec les mêmes pièces et positions
     */
    @NotNull
    public PlateauPiece getCopie() {
        return new PlateauPiece(board);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (Piece piece : board.values()) {
            stringBuilder.append(getPosition(piece)).append(" ").append(piece).append(", ");
        }

        return stringBuilder.append("]").toString();
    }

    @NotNull
    public Set<Mouvement> getAllMoves(Couleur couleur) {
        Set<Mouvement> mouvements = new HashSet<>();

        for (Piece piece : board.values()) {
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
