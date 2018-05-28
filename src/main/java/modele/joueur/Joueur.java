package modele.joueur;

import modele.MoveCallbackWrapper;
import modele.pieces.Couleur;

public interface Joueur {
    void notifierTour(MoveCallbackWrapper moveCallbackWrapper);

    Couleur getCouleur();
}
