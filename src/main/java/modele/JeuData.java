package modele;

import modele.moves.Mouvement;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class JeuData implements Serializable {
    @NotNull
    public final Plateau plateau;

    @Nullable
    private Consumer<Plateau> changeListener;

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull Plateau plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    public void setChangeListener(@NotNull Consumer<Plateau> changeListener) {
        this.changeListener = changeListener;
    }

    void notifyListenerOfChange(Plateau plateau) {
        changeListener.accept(plateau);
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
        return filterOnlyLegal(plateau.getAllMoves(couleur, this), couleur);
    }

    @NotNull
    public Set<Mouvement> filterOnlyLegal(Set<Mouvement> mouvements, Couleur verifierPour) {
        Set<Mouvement> legalMouvements = new HashSet<>();

        Plateau tempPlateau = plateau.getCopie();

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(tempPlateau);

            if (!tempPlateau.isPieceAttaquer(getRoi(verifierPour))) {
                legalMouvements.add(mouvement);
            }

            mouvement.undo(tempPlateau);
        }

        return legalMouvements;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Consumer<Plateau> listener = changeListener;
        changeListener = null;
        out.defaultWriteObject();
        changeListener = listener;
    }
}