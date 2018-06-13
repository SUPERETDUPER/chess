package ui.game;

import model.GameData;
import model.moves.Move;
import model.player.Player;
import model.util.Colour;

import java.util.function.Consumer;

/**
 * A player that uses the UI to submit his moves
 */
public class HumanPlayer extends Player {
    /**
     * The UI
     */
    transient private BoardPane boardPane;

    @Override
    public void initializeGameData(GameData gameData) {

    }

    public void attachUI(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * Requests the UI (BoardPane) to allow the user to submit his move
     *
     * @param callback the callback method where the selected move should eventually be submitted
     * @param colour the colour of the player that should submit his move
     */
    @Override
    public void getMove(Consumer<Move> callback, Colour colour) {
        boardPane.requestMove(callback, colour); //Create a request and submit to the UI
    }

    @Override
    public String getName() {
        return "Humain";
    }
}
