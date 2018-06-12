package model.player;

import model.GameData;
import model.moves.Move;
import model.util.Colour;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Un player. Définit comment il va player
 */
public abstract class Player implements Serializable {
    /**
     * Demande au player de soumettre son prochain moves par l'entremise du callback
     *
     * @param callback la méthode par laquelle l'on soumet son prochain moves
     * @param colour  la colour du moves requis
     */
    public abstract void getMouvement(Consumer<Move> callback, Colour colour);

    /**
     * @param gameData le gamewindow data
     */
    public abstract void initializeJeuData(GameData gameData);

    /**
     * @return Le nom du player tel que affiché sur l'interface
     */
    protected abstract String getNom();

    @Override
    public String toString() {
        return getNom();
    }
}
