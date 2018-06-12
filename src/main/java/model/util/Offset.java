package model.util;

import java.io.Serializable;

/**
 * Un déplacement relatif
 */
public class Offset implements Serializable {
    public static final Offset TOP_LEFT = new Offset(-1, -1);
    public static final Offset UP = new Offset(-1, 0);
    public static final Offset TOP_RIGHT = new Offset(-1, 1);
    public static final Offset LEFT = new Offset(0, -1);
    public static final Offset RIGHT = new Offset(0, 1);
    public static final Offset BOTTOM_LEFT = new Offset(1, -1);
    public static final Offset DOWN = new Offset(1, 0);
    public static final Offset BOTTOM_RIGHT = new Offset(1, 1);

    private final int[] offset = new int[2];

    /**
     * @param offsetVertical   montant à se déplacer vers le bas
     * @param offsetHorizontal montant à se déplacer vers la droite
     */
    public Offset(int offsetVertical, int offsetHorizontal) {
        offset[0] = offsetVertical;
        offset[1] = offsetHorizontal;
    }

    int getVerticalShift() {
        return offset[0];
    }

    int getHorizontalShift() {
        return offset[1];
    }
}
