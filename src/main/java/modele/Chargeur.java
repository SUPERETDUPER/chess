package modele;

import java.io.*;

public class Chargeur {

    private File file = new File("dernierePartie.txt");

    public void sauvgarder(Jeu jeu) {
        try {
            if (!file.exists()) file.createNewFile();

            new ObjectOutputStream(new FileOutputStream(file)).writeObject(jeu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGame() {
        return file.exists();
    }

    public Jeu charger() {
        try {
            return (Jeu) new ObjectInputStream(new FileInputStream(file)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
