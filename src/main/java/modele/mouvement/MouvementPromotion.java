package modele.mouvement;

import modele.pieces.Reine;
import modele.util.Position;
import org.jetbrains.annotations.NotNull;

/**
 * Un mouvement qui promouvoit un pion à une reine en la bougeant
 */
public class MouvementPromotion extends MouvementNormal {
    public MouvementPromotion(Position debut, @NotNull Position fin) {
        super(debut, fin);
    }

    @Override
    public int getValeur() {
        return super.getValeur() + new Reine(piece.getCouleur()).getValeur() - piece.getValeur();
    }
}
