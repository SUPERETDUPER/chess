package engine

import engine.player.Player
import engine.util.Colour
import engine.util.PieceMap
import java.io.*
import java.util.*

/**
 * Loads and saves the game to a file to allow the game to be saved between JVM runs
 */
class Loader {
    /**
     * The file to load/save to
     */
    private val file = File("savedGame.txt")

    /**
     * The loaded game
     */
    private lateinit var _game: Game

    var game: Game
        get() = _game
        private set(value) {
            value.addBoardChangeListener(::saveGame) //Add listener such that when the game changes, it is saved
            _game = value
        }

    /**
     * Saves the game
     */
    private fun saveGame() {
        try {

            file.createNewFile() //Create a new file (fails silently if file already exists)

            //Write the game to the file
            val objectOutputStream = ObjectOutputStream(FileOutputStream(file))
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Tries loading the game to the file.
     * If succeeds will save the game to this.game
     *
     * @return true if succeeded in loading the game
     */
    fun loadGameFromFile(): Boolean {
        if (!file.exists()) return false //If file does not exist there is no game to load

        return try {
            val objectInputStream = ObjectInputStream(FileInputStream(file))
            this.game = objectInputStream.readObject() as Game //Lire le game
            objectInputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun createNewGame(players: EnumMap<Colour, Player>) {
        //Create the game state (in starting position)
        val gameData = GameData(PieceMap.createStartingBoard())

        //Create the game (players + state)
        game = Game(gameData, players)
    }
}
