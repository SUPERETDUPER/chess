package modele.joueur;

import modele.Modele;

public interface Joueur {
    void notifierTour(Modele.MoveCallback moveCallback);
}
