package modele.plateau;

public class Offset {
    public static final Offset HAUT_GAUGHE = new Offset(-1, -1);
    public static final Offset HAUT_CENTRE = new Offset(-1, 0);
    public static final Offset HAUT_DROIT = new Offset(-1, 1);
    public static final Offset MILIEU_GAUCHE = new Offset(0, -1);
    public static final Offset MILIEU_DROIT = new Offset(0, 1);
    public static final Offset BAS_GAUCHE = new Offset(1, -1);
    public static final Offset BAS_CENTRE = new Offset(1, 0);
    public static final Offset BAS_DROIT = new Offset(1, 1);

    private final int[] offset = new int[2];

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
