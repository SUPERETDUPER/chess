package modele.joueur;

import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.function.Consumer;

public interface Joueur {
    void getMouvement(Consumer<Move> callback);

    Couleur getCouleur();
}
