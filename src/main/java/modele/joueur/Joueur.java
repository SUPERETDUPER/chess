package modele.joueur;

import modele.MoveCallbackWrapper;
import modele.pieces.Couleur;

public interface Joueur {
    void getMouvement(MoveCallbackWrapper moveCallbackWrapper);

    Couleur getCouleur();
}
