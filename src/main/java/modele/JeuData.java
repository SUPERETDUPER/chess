package modele;

import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

//TODO Move joueur to jeu
public class JeuData {
    @NotNull
    private final Plateau plateau;

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull Plateau plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    @NotNull
    public Plateau getPlateau() {
        return plateau;
    }

    @NotNull
    Roi getRoi(Couleur couleur) {
        return rois.get(couleur);
    }

    @NotNull
    public Set<Move> getAllLegalMoves(Couleur couleur) {
        return filterOnlyLegal(getAllMoves(couleur), couleur);
    }

    @NotNull
    public Set<Move> getAllMoves(Couleur couleur) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : plateau.iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                moves.addAll(piece.generateAllMoves(plateau));
            }
        }
        return moves;
    }

    @NotNull
    public Set<Move> filterOnlyLegal(Set<Move> moves, Couleur verifierPour) {
        Set<Move> legalMoves = new HashSet<>();

        Plateau tempPlateau = plateau.getCopie();

        for (Move move : moves) {
            move.appliquer(tempPlateau);

            if (!Helper.isPieceAttaquer(tempPlateau, getRoi(verifierPour))) {
                legalMoves.add(move);
            }

            move.undo(tempPlateau);
        }

        return legalMoves;
    }
}