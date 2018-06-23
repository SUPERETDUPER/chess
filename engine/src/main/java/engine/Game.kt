package engine

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import engine.moves.Move
import engine.player.Player
import engine.util.Colour
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.*

/**
 * Class at the top of the hierarchy that organises when each player plays and verifies for checkmate/stalemates.
 *
 *
 * When a player plays, the next player doesn't play automatically. Instead the notifyNextPlayerMethod needs to be called. This allows the UI to update first.
 *
 * @property gameData the game state
 * @property players the mapping of players based on their color
 */
class Game internal constructor(val gameData: GameData, val players: EnumMap<Colour, Player>) : Serializable {

    /**
     * Who's turn it is
     */
    @Transient
    private var turnMarker = ReadOnlyObjectWrapper(Colour.WHITE)


    @Transient
    private var status = ReadOnlyObjectWrapper(Status.INACTIVE)

    /**
     * The listener to notify when the board has changed
     */
    @Transient
    private var boardChangeListeners: MutableCollection<() -> Unit> = ArrayList()

    /**
     * The listener of the result
     */
    @Transient
    private var resultListener: ((Result) -> Unit)? = null

    /**
     * The result of the game
     */
    enum class Result {
        WHITE_WINS,
        BLACK_WINS,
        TIE
    }

    /**
     * The status of the game (either waiting on a player to call the callback or inactive
     */
    enum class Status {
        WAITING,
        INACTIVE
    }

    init {
        for (player in players.values) {
            player.initializeGameData(gameData)
        }
    }

    fun addBoardChangeListener(boardChangeListener: () -> Unit) {
        this.boardChangeListeners.add(boardChangeListener)
    }

    /**
     * Asks the next player to play
     */
    fun notifyNextPlayer() {
        if (status.get() != Status.INACTIVE)
            throw RuntimeException("Game is not inactive. Should not request move from player")

        //TODO move check mate verification to the play and make sure no error is thrown on game finish
        //Verifies is the game state is a checkmate/stalemate
        val moves = gameData.getPossibleLegalMoves(turnMarker.get())

        if (moves.isEmpty()) {
            if (gameData.isPieceAttacked(gameData.getKing(turnMarker.get()))) {
                if (turnMarker.get() == Colour.BLACK) {
                    resultListener!!.invoke(Result.WHITE_WINS)
                } else {
                    resultListener!!.invoke(Result.BLACK_WINS)
                }
            } else {
                resultListener!!.invoke(Result.TIE)
            }
            return
        }

        //Switch the status and notify the player
        status.set(Status.WAITING)
        players[turnMarker.get()]!!.getMove(this::submitMove, turnMarker.get()) //Request that the next player submits his move
    }

    fun setResultListener(resultListener: (Result) -> Unit) {
        this.resultListener = resultListener
    }

    /**
     * Called by a player to submit its move
     *
     * @param move the submitted move
     */
    private fun submitMove(move: Move) {
        move.apply(gameData) //Apply the move to the state

        switchTurn()
        notifyListeners()

        status.set(Status.INACTIVE)
    }

    private fun switchTurn() {
        turnMarker.set(if (turnMarker.value == Colour.WHITE) Colour.BLACK else Colour.WHITE)
    }

    /**
     * Undoes a certain number of moves
     *
     * @param tour the number of turns to undo
     */
    fun undo(tour: Int) {
        for (i in 0 until tour) {
            if (gameData.pastMoves.isEmpty()) break //If none left break
            gameData.pastMoves.last.undo(gameData)
            switchTurn()
        }

        notifyListeners()

        status.set(Status.INACTIVE)
    }

    private fun notifyListeners() {
        for (boardChangeListener in boardChangeListeners) {
            boardChangeListener.invoke()
        }
    }

    fun statusProperty(): ReadOnlyObjectProperty<Status> {
        return status.readOnlyProperty
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
        out.writeObject(turnMarker.get())
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
        turnMarker = ReadOnlyObjectWrapper(`in`.readObject() as Colour)
        status = ReadOnlyObjectWrapper(Status.INACTIVE)
        boardChangeListeners = ArrayList()
    }
}