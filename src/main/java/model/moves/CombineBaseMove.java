package model.moves;

import model.GameData;
import model.util.Position;

/**
 * Un moves qui est un ensemble d'autre mouvements
 * La liste de mouvements est appliqué en ordre. Et la valeur est la somme
 */
public class CombineBaseMove extends BaseMove {
    private final Move[] mouvementsSupplementaires;
    private Integer valeur;

    public CombineBaseMove(Position debut, Position fin, Move[] mouvementsSupplementaires) {
        super(debut, fin);
        this.mouvementsSupplementaires = mouvementsSupplementaires;
    }

    @Override
    void appliquerInterne(GameData data) {
        super.appliquerInterne(data);

        for (Move move : mouvementsSupplementaires) {
            move.appliquer(data);
        }
    }

    @Override
    void undoInterne(GameData data) {
        for (int i = (mouvementsSupplementaires.length - 1); i >= 0; i--) {
            mouvementsSupplementaires[i].undo(data);
        }

        super.undoInterne(data);
    }

    @Override
    public int getValeur() {
        if (valeur == null) {
            valeur = super.getValeur();

            for (Move move : mouvementsSupplementaires) {
                valeur += move.getValeur();
            }
        }

        return valeur;
    }
}
