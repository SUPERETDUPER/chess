package modele;

import modele.moves.Mouvement;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class JeuData {
    @NotNull
    private final Plateau plateau;

    private Consumer<Mouvement> newChangeListener;

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull Plateau plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    public void setNewChangeListener(Consumer<Mouvement> newChangeListener) {
        this.newChangeListener = newChangeListener;
    }

    void notifyListenerOfChange(Mouvement mouvement) {
        newChangeListener.accept(mouvement);
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
    public Set<Mouvement> getAllLegalMoves(Couleur couleur) {
        return filterOnlyLegal(getAllMoves(couleur), couleur);
    }

    @NotNull
    public Set<Mouvement> getAllMoves(Couleur couleur) {
        Set<Mouvement> mouvements = new HashSet<>();

        for (Piece piece : plateau.iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                mouvements.addAll(piece.generateAllMoves(plateau));
            }
        }
        return mouvements;
    }

    @NotNull
    public Set<Mouvement> filterOnlyLegal(Set<Mouvement> mouvements, Couleur verifierPour) {
        Set<Mouvement> legalMouvements = new HashSet<>();

        Plateau tempPlateau = plateau.getCopie();

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(tempPlateau);

            if (!Helper.isPieceAttaquer(tempPlateau, getRoi(verifierPour))) {
                legalMouvements.add(mouvement);
            }

            mouvement.undo(tempPlateau);
        }

        return legalMouvements;
    }
}