package gui.gamewindow.boardregion;

import model.GameData;
import model.moves.Move;
import model.player.Player;
import model.util.Colour;

import java.util.function.Consumer;

/**
 * Un player qui utilise le util de gamewindow pour soumettre ses mouvements
 */
public class HumanPlayer extends Player {
    transient private BoardPane boardPane;

    @Override
    public void initializeGameData(GameData gameData) {

    }

    public void initializeInterface(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le util de gamewindow
     *
     * @param callback la méthode par laquelle l'on soumet son prochain moves
     */
    @Override
    public void getMove(Consumer<Move> callback, Colour colour) {
        boardPane.requestMove(new MoveRequest(callback, colour));
    }

    @Override
    public String getName() {
        return "Humain";
    }
}
