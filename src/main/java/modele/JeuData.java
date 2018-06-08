package modele;

import modele.moves.Mouvement;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class JeuData implements Serializable {
    @NotNull
    public final Plateau plateau;

    private final List<Consumer<Plateau>> changeListeners = new ArrayList<>();

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull Plateau plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    public void addChangeListener(@NotNull Consumer<Plateau> changeListener) {
        changeListeners.add(changeListener);
    }

    void notifyListenerOfChange(Plateau plateau) {
        for (Consumer<Plateau> listener : changeListeners) {
            listener.accept(plateau);
        }
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
        //Pour que les listeners externe ne soit pas sauvegardé
        @SuppressWarnings("UnnecessaryLocalVariable") List<Consumer<Plateau>> listeners = changeListeners;
        changeListeners.clear();
        out.defaultWriteObject();
        changeListeners.addAll(listeners);
    }
}