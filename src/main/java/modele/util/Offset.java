package modele.util;

import java.io.Serializable;

/**
 * Un déplacement relatif
 */
public class Offset implements Serializable {
    public static final Offset HAUT_GAUGHE = new Offset(-1, -1);
    public static final Offset HAUT = new Offset(-1, 0);
    public static final Offset HAUT_DROIT = new Offset(-1, 1);
    public static final Offset GAUCHE = new Offset(0, -1);
    public static final Offset DROIT = new Offset(0, 1);
    public static final Offset BAS_GAUCHE = new Offset(1, -1);
    public static final Offset BAS = new Offset(1, 0);
    public static final Offset BAS_DROIT = new Offset(1, 1);

    private final int[] offset = new int[2];

    /**
     * @param offsetVertical   montant à se déplacer vers le bas
     * @param offsetHorizontal montant à se déplacer vers la droite
     */
    public Offset(int offsetVertical, int offsetHorizontal) {
        offset[0] = offsetVertical;
        offset[1] = offsetHorizontal;
    }

    public int getOffsetVertical() {
        return offset[0];
    }

    public int getOffsetHorizontal() {
        return offset[1];
    }
}
