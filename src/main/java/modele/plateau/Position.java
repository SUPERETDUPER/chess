package modele.plateau;

public interface Position {
    int getRangee();

    int getColonne();

    boolean isValid();

    Position decaler(Offset offset);
}
