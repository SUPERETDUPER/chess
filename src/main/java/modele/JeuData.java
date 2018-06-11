package modele;

import modele.mouvement.Mouvement;
import modele.pieces.Roi;
import modele.util.Couleur;
import modele.util.Plateau;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Représente le plateau de jeu et quelle pièces sont les rois
 */
public class JeuData implements Serializable {
    @NotNull
    private final Plateau plateau;

    /**
     * Le listener pour quand le plateau change
     */
    @Nullable
    transient private Consumer<Plateau> changeListener;

    /**
     * La liste de roi associé à chaque couleur
     */
    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    /**
     * @param plateau     le plateau de jeu
     * @param premierRoi  le premier roi
     * @param deuxiemeRoi le deuxieme roi
     */
    public JeuData(@NotNull Plateau plateau, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.plateau = plateau;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    public void setChangeListener(@NotNull Consumer<Plateau> changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * Appelé par {@link Jeu} pour notifier qu'un mouvement sur le plateau a été effectué
     *
     * @param plateau le plateau après le mouvement
     */
    //TODO Ne devraient pas contenir plateau comme paramettre. Il faut que le prochain joueur calcule le mouvement seulement quand l'interface décide
    void notifyListenerOfChange(Plateau plateau) {
        changeListener.accept(plateau);
    }

    @NotNull
    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * @param couleur la couleur du roi demandé
     */
    @NotNull
    Roi getRoi(Couleur couleur) {
        return rois.get(couleur);
    }

    /**
     * @return la liste mouvements possible et légal pour cette couleur
     */
    @NotNull
    public List<Mouvement> getAllLegalMoves(Couleur couleur) {
        return filterOnlyLegal(plateau.getAllMoves(couleur), couleur);
    }

    /**
     * @param mouvements une liste de mouvement légals et illégals
     * @param verifierPour la couleur du joueur qu'il faut vérifier
     * @return la liste de mouvements avec que les mouvements légals
     */
    @NotNull
    public List<Mouvement> filterOnlyLegal(Set<Mouvement> mouvements, Couleur verifierPour) {
        List<Mouvement> legalMouvements = new ArrayList<>();

        Plateau tempPlateau = plateau.getCopie();

        //Pour chaque mouvement appliquer et vérifier si l'on attaque le roi
        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(tempPlateau);

            if (!tempPlateau.isPieceAttaquer(rois.get(verifierPour))) {
                legalMouvements.add(mouvement);
            }

            mouvement.undo(tempPlateau);
        }

        return legalMouvements;
    }
}