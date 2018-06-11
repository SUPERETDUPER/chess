package modele.mouvement;

import modele.JeuData;

/**
 * Un mouvement qui est un ensemble d'autre mouvements
 * La liste de mouvements est appliqué en ordre. Et la valeur est la somme
 */
public class MouvementCombine extends Mouvement {
    private final Mouvement[] mouvements;
    private Integer valeur;

    public MouvementCombine(Mouvement[] mouvements) {
        super(mouvements[0].piece, mouvements[0].fin);
        this.mouvements = mouvements;
    }

    @Override
    void appliquerInterne(JeuData data) {
        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(data);
        }
    }

    @Override
    void undoInterne(JeuData data) {
        for (int i = (mouvements.length - 1); i >= 0; i--) {
            mouvements[i].undo(data);
        }
    }

    @Override
    public int getValeur() {
        if (valeur == null) {
            valeur = 0;

            for (Mouvement mouvement : mouvements) {
                valeur += mouvement.getValeur();
            }
        }

        return valeur;
    }
}
