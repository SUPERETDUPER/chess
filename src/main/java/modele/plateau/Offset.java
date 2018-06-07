package modele.plateau;

import java.io.Serializable;

/**
 * Un déplacement relatif
 */
public class Offset implements Serializable {
    public static final Offset HAUT_GAUGHE = new Offset(-1, -1);
    public static final Offset HAUT_CENTRE = new Offset(-1, 0);
    public static final Offset HAUT_DROIT = new Offset(-1, 1);
    public static final Offset MILIEU_GAUCHE = new Offset(0, -1);
    public static final Offset MILIEU_DROIT = new Offset(0, 1);
    public static final Offset BAS_GAUCHE = new Offset(1, -1);
    public static final Offset BAS_CENTRE = new Offset(1, 0);
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

    int getOffsetVertical() {
        return offset[0];
    }

    int getOffsetHorizontal() {
        return offset[1];
    }
}
