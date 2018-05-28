package modele.joueur;

import modele.MoveCallbackWrapper;

public interface Joueur {
    void notifierTour(MoveCallbackWrapper moveCallbackWrapper);
}
