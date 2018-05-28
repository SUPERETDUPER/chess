package modele.joueur;

import modele.MoveEvent;

public interface Joueur {
    void notifierTour(MoveEvent moveEvent);
}
