package model;

import model.player.Player;
import model.util.BoardMap;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.util.EnumMap;

/**
 * Loads and saves the game to a file to allow the game to be saved between JVM runs
 */
public class Loader {
    /**
     * The file to load/save to
     */
    private final File file = new File("savedGame.txt");

    /**
     * The loaded game
     */
    @Nullable
    private Game game;

    /**
     * Saves the game
     */
    private void saveGame() {
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile(); //Create a new file (fails silently if file already exists)

            //Write the game to the file
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tries loading the game to the file.
     * If succeeds will save the game to this.game
     *
     * @return true if succeeded in loading the game
     */
    public boolean loadGameFromFile() {
        if (!file.exists()) return false; //If file does not exist there is no game to load

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            this.game = (Game) objectInputStream.readObject(); //Lire le game
            this.game.addBoardChangeListener(this::saveGame); //Add listener such that when the game changes, it is saved
            objectInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createNewGame(@NotNull EnumMap<Colour, Player> players) {
        //Create the game state (in starting position)
        GameData gameData = new GameData(BoardMap.createStartingBoard());

        //Create the game (players + state)
        this.game = new Game(gameData, players);
        game.addBoardChangeListener(this::saveGame);//Add listener such that when the game changes, it is saved
    }

    /**
     * @return null if loadGameFromFile and/or createNewGame was not called
     */
    @Nullable
    public Game getGame() {
        return game;
    }
}
