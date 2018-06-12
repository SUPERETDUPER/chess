package modele;

import modele.mouvement.Mouvement;
import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.util.Couleur;
import modele.util.Plateau;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Stack;

/**
 * Représente le plateau de jeu et quelle pièces sont les rois
 */
public class JeuData implements Serializable {
    @NotNull
    private final Plateau plateau;

    @NotNull
    private final Stack<Piece> eatenPieces;

    /**
     * Le listener pour quand le plateau change
     */
    @Nullable
    transient private Runnable changeListener;

    /**
     * La liste de roi associé à chaque couleur
     */
    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    /**
     * @param plateau     le plateau de jeu
     *
     */
    public JeuData(@NotNull Plateau plateau) {
        this(plateau, new Stack<>());
    }

    /**
     * @param plateau le plateau de jeu
     */
    private JeuData(@NotNull Plateau plateau, @NotNull Stack<Piece> eatenPieces) {
        for (Piece piece : plateau.iteratePieces()) {
            if (piece instanceof Roi) {
                if (rois.containsKey(piece.getCouleur()))
                    throw new RuntimeException("Il y a deux rois de la même couleur");

                rois.put(piece.getCouleur(), (Roi) piece);
            }
        }

        this.plateau = plateau;
        this.eatenPieces = eatenPieces;
    }

    public void setChangeListener(@NotNull Runnable changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * Appelé par {@link Jeu} pour notifier qu'un mouvement sur le plateau a été effectué
     */
    void notifyListenerOfChange() {
        changeListener.run();
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
    public Collection<Mouvement> getAllLegalMoves(Couleur couleur) {
        return filterOnlyLegal(plateau.getAllMoves(couleur), couleur);
    }

    /**
     * @param mouvements une liste de mouvement légals et illégals
     * @param verifierPour la couleur du joueur qu'il faut vérifier
     * @return la liste de mouvements avec que les mouvements légals
     */
    @NotNull
    public Collection<Mouvement> filterOnlyLegal(Collection<Mouvement> mouvements, Couleur verifierPour) {
        Collection<Mouvement> legalMouvements = new ArrayList<>();

        JeuData copie = getCopie();

        //Pour chaque mouvement appliquer et vérifier si l'on attaque le roi
        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(copie);

            if (!copie.getPlateau().isPieceAttaquer(rois.get(verifierPour))) {
                legalMouvements.add(mouvement);
            }

            mouvement.undo(copie);
        }

        return legalMouvements;
    }

    private JeuData getCopie() {
        return new JeuData(plateau.getCopie(), eatenPieces);
    }

    @NotNull
    public Stack<Piece> getEatenPieces() {
        return eatenPieces;
    }
}