package modele;

import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class JeuData {
    @NotNull
    private final Plateau plateau;

    @NotNull
    private final EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);

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
    public Joueur getJoueur(Couleur couleur) {
        return joueurs.get(couleur);
    }

    @NotNull
    public Roi getRoi(Couleur couleur) {
        return rois.get(couleur);
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.put(joueur.getCouleur(), joueur);
    }

    @NotNull
    public Set<Move> getAllLegalMoves(Couleur couleur) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : getPlateau().iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                moves.addAll(piece.getLegalMoves(this));
            }
        }

        return moves;
    }

    public boolean roiInCheck(Couleur couleurDuRoi, Plateau plateau) {
        for (Piece piece : plateau.iteratePieces()) {
            if (piece.getCouleur() != couleurDuRoi && piece.attacksPosition(plateau, plateau.getPosition(getRoi(couleurDuRoi)))) {
                return true;
            }
        }

        return false;
    }

    public boolean roiInCheck(Couleur couleurDuRoi) {
        return roiInCheck(couleurDuRoi, plateau);
    }
}
