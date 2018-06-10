package modele;

import modele.moves.Mouvement;
import modele.pieces.Roi;
import modele.plateau.PlateauPiece;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class JeuData implements Serializable {
    @NotNull
    public PlateauPiece plateau;

    @NotNull
    private EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull PlateauPiece plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    @NotNull
    public PlateauPiece getPlateau() {
        return plateau;
    }

    @NotNull
    Roi getRoi(Couleur couleur) {
        return rois.get(couleur);
    }

    @NotNull
    public Set<Mouvement> getAllLegalMoves(Couleur couleur) {
        return filterOnlyLegal(plateau.getAllMoves(couleur), couleur);
    }

    @NotNull
    public Set<Mouvement> filterOnlyLegal(Set<Mouvement> mouvements, Couleur verifierPour) {
        Set<Mouvement> legalMouvements = new HashSet<>();

        PlateauPiece tempPlateau = plateau.getCopie();

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(tempPlateau);

            if (!tempPlateau.isPieceAttaquer(getRoi(verifierPour))) {
                legalMouvements.add(mouvement);
            }

            mouvement.undo(tempPlateau);
        }

        return legalMouvements;
    }
}