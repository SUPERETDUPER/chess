package model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import model.moves.Move;
import model.player.Player;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.function.Consumer;

/**
 * Class at the top of the hierarchy that organises when each player plays and verifies for checkmate/stalemates.
 *
 * When a player plays, the next player doesn't play automatically. Instead the notifyNextPlayerMethod needs to be called. This allows the UI to update first.
 */
public class Game implements Serializable {
    /**
     * The result of the game
     */
    public enum Result {
        WHITE_WINS,
        BLACK_WINS,
        TIE
    }

    /**
     * The status of the game (either waiting on a player to call the callback or inactive
     */
    public enum Status {
        WAITING,
        INACTIVE
    }

    /**
     * The actual game state
     */
    private final GameData gameData;

    /**
     * The mapping of players based on their color
     */
    @NotNull
    private final EnumMap<Colour, Player> players;

    /**
     * Who's turn it is
     */
    transient private ReadOnlyObjectWrapper<Colour> turnMarker = new ReadOnlyObjectWrapper<>(Colour.WHITE);


    transient private ReadOnlyObjectWrapper<Status> status = new ReadOnlyObjectWrapper<>(Status.INACTIVE);

    /**
     * The listener to notify when the board has changed
     */
    transient private Collection<Runnable> boardChangeListeners = new ArrayList<>();

    /**
     * The listener of the result
     */
    transient private Consumer<Result> resultListener;

    /**
     * @param gameData the game state (piece's position)
     * @param players  the players
     */
    Game(GameData gameData, @NotNull EnumMap<Colour, Player> players) {
        this.gameData = gameData;
        this.players = players;

        for (Player player : players.values()) {
            player.initializeGameData(gameData);
        }
    }

    public void addBoardChangeListener(Runnable boardChangeListener) {
        this.boardChangeListeners.add(boardChangeListener);
    }

    /**
     * Asks the next player to play
     */
    public void notifyNextPlayer() {
        if (status.get() != Status.INACTIVE) throw new RuntimeException("Game is not inactive. Should not request move from player");

        //TODO move check mate verification to the play and make sure no error is thrown on game finish
        //Verifies is the game state is a checkmate/stalemate
        Collection<Move> moves = gameData.getPossibleLegalMoves(turnMarker.get());

        if (moves.isEmpty()) {
            if (gameData.isPieceAttacked(gameData.getKing(turnMarker.get()))) {
                if (turnMarker.get() == Colour.BLACK) {
                    resultListener.accept(Result.WHITE_WINS);
                } else {
                    resultListener.accept(Result.BLACK_WINS);
                }
            } else {
                resultListener.accept(Result.TIE);
            }
            return;
        }

        //Switch the status and notify the player
        status.set(Status.WAITING);
        players.get(turnMarker.get()).getMove(this::submitMove, turnMarker.get()); //Demander au player de bouger
    }

    public void setResultListener(Consumer<Result> resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * Called by a player to submit its move
     *
     * @param move the submited move
     */
    private void submitMove(@NotNull Move move) {
        move.apply(gameData); //Apply the move to the state
        gameData.getPastMoves().add(move); //Add the move to the list

        switchTurn();
        notifyListeners();

        status.set(Status.INACTIVE);
    }

    private void switchTurn() {
        turnMarker.set(turnMarker.getValue() == Colour.WHITE ? Colour.BLACK : Colour.WHITE);
    }

    /**
     * Undoes a certain number of moves
     * @param tour the number of turns to undo
     */
    public void undo(int tour) {
        for (int i = 0; i < tour; i++) {
            gameData.getPastMoves().removeLast().undo(gameData);
            switchTurn();
        }

        notifyListeners();

        status.set(Status.INACTIVE);
    }

    private void notifyListeners() {
        for (Runnable boardChangeListener : boardChangeListeners) {
            boardChangeListener.run();
        }
    }

    public GameData getGameData() {
        return gameData;
    }

    @NotNull
    public EnumMap<Colour, Player> getPlayers() {
        return players;
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return status.getReadOnlyProperty();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(turnMarker.get());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        turnMarker = new ReadOnlyObjectWrapper<>((Colour) in.readObject());
        status = new ReadOnlyObjectWrapper<>(Status.INACTIVE);
        boardChangeListeners = new ArrayList<>();
    }
}