package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.pieces.Couleur;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class JeuData {
    @NotNull
    private final Board board;

    @NotNull
    private final EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    public JeuData(@NotNull Board board, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.board = board;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    @NotNull
    public Board getBoard() {
        return board;
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
}
