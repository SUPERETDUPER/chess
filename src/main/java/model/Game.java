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
import java.util.Collection;
import java.util.EnumMap;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Classe qui supervise les players et s'assure de respecter les tours
 */
public class Game implements Serializable {
    /**
     * Le résultat de la partie
     */
    public enum Result {
        WHITE_WINS,
        BLACK_WINS,
        TIE
    }

    public enum Status {
        WAITING_FOR_WHITE,
        WAITING_FOR_BLACK,
        INACTIVE
    }

    /**
     * Le boardregion et les rois
     */
    private final GameData gameData;

    /**
     * La liste de players
     */
    @NotNull
    private final EnumMap<Colour, Player> players;

    /**
     * A qui le tour
     */
    transient private ReadOnlyObjectWrapper<Colour> turnMarker = new ReadOnlyObjectWrapper<>(Colour.WHITE);

    transient private ReadOnlyObjectWrapper<Status> status = new ReadOnlyObjectWrapper<>(Status.INACTIVE);

    /**
     * le listener de resultat
     */
    transient private Consumer<Result> resultListener;

    /**
     * la liste de pastMoves effectuées
     */
    private final Stack<Move> pastMoves = new Stack<>();

    /**
     * @param gameData l'info de game
     * @param players les players
     */
    Game(GameData gameData, @NotNull EnumMap<Colour, Player> players) {
        this.gameData = gameData;
        this.players = players;

        for (Player player : players.values()) {
            player.initializeGameData(gameData);
        }
    }

    /**
     * Commencer la partie
     */
    public void notifyNextPlayer() {
        //Vérifier pour échec et mat ou match nul
        Collection<Move> moves = gameData.getAllLegalMoves(turnMarker.get());

        if (moves.isEmpty()) {
            if (gameData.getBoard().isPieceAttacked(gameData.getKing(turnMarker.get()))) {
                if (turnMarker.get() == Colour.BLACK) {
                    resultListener.accept(Result.WHITE_WINS);
                } else {
                    resultListener.accept(Result.BLACK_WINS);
                }
            } else {
                resultListener.accept(Result.TIE);
            }
        } else {
            //Si la partie n'est pas fini notifier prochain player
            status.set(turnMarker.get() == Colour.WHITE ? Status.WAITING_FOR_WHITE : Status.WAITING_FOR_BLACK);
            players.get(turnMarker.get()).getMove(this::submitMove, turnMarker.get()); //Demander au player de bouger
        }
    }

    public void setResultListener(Consumer<Result> resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * Appelé par le callback de player.getMove()
     *
     * @param move le pastMoves à submitMove
     */
    private void submitMove(@NotNull Move move) {
        move.apply(gameData); //Jouer le pastMoves
        pastMoves.push(move); //Ajouter à la liste

        gameData.notifyListenerOfChange(); //Notifier changement

        switchTurn();

        status.set(Status.INACTIVE);
    }

    private void switchTurn() {
        turnMarker.set(turnMarker.getValue() == Colour.WHITE ? Colour.BLACK : Colour.WHITE); //Changer le tour
    }

    /**
     * @param tour le nombre de pastMoves à défaire
     */
    public void undo(int tour) {
        for (int i = 0; i < tour; i++) {
            pastMoves.pop().undo(gameData);
            gameData.notifyListenerOfChange();
            switchTurn();
        }

        status.set(Status.INACTIVE);
    }

    public GameData getGameData() {
        return gameData;
    }

    @NotNull
    public EnumMap<Colour, Player> getPlayers() {
        return players;
    }

    ReadOnlyObjectProperty<Colour> turnMarkerProperty() {
        return turnMarker.getReadOnlyProperty();
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
    }
}