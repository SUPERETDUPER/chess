package modele.mouvement;

import modele.JeuData;
import modele.util.Position;

/**
 * Un mouvement qui est un ensemble d'autre mouvements
 * La liste de mouvements est appliqué en ordre. Et la valeur est la somme
 */
public class MouvementCombine extends MouvementNormal {
    private final Mouvement[] mouvementsSupplementaires;
    private Integer valeur;

    public MouvementCombine(Position debut, Position fin, Mouvement[] mouvementsSupplementaires) {
        super(debut, fin);
        this.mouvementsSupplementaires = mouvementsSupplementaires;
    }

    @Override
    void appliquerInterne(JeuData data) {
        super.appliquerInterne(data);

        for (Mouvement mouvement : mouvementsSupplementaires) {
            mouvement.appliquer(data);
        }
    }

    @Override
    void undoInterne(JeuData data) {
        for (int i = (mouvementsSupplementaires.length - 1); i >= 0; i--) {
            mouvementsSupplementaires[i].undo(data);
        }

        super.undoInterne(data);
    }

    @Override
    public int getValeur() {
        if (valeur == null) {
            valeur = super.getValeur();

            for (Mouvement mouvement : mouvementsSupplementaires) {
                valeur += mouvement.getValeur();
            }
        }

        return valeur;
    }
}
