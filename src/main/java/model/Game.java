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
 * Classe qui supervise les joueurs et s'assure de respecter les tours
 */
public class Game implements Serializable {
    /**
     * Le résultat de la partie
     */
    public enum Resultat {
        BLANC_GAGNE,
        NOIR_GAGNE,
        EGALITE
    }

    public enum Status {
        ATTENTE_BLANC,
        ATTENTE_NOIR,
        INACTIF
    }

    /**
     * Le boardregion et les rois
     */
    private final GameData gameData;

    /**
     * La liste de joueurs
     */
    @NotNull
    private final EnumMap<Colour, Player> joueurs;

    /**
     * A qui le tour
     */
    transient private ReadOnlyObjectWrapper<Colour> tourA = new ReadOnlyObjectWrapper<>(Colour.BLANC);

    transient private ReadOnlyObjectWrapper<Status> status = new ReadOnlyObjectWrapper<>(Status.INACTIF);

    /**
     * le listener de resultat
     */
    transient private Consumer<Resultat> resultatListener;

    /**
     * la liste de moves effectuées
     */
    private final Stack<Move> moves = new Stack<>();

    /**
     * @param gameData l'info de gamewindow
     * @param joueurs les joueurs
     */
    Game(GameData gameData, @NotNull EnumMap<Colour, Player> joueurs) {
        this.gameData = gameData;
        this.joueurs = joueurs;

        for (Player player : joueurs.values()) {
            player.initializeJeuData(gameData);
        }
    }

    /**
     * Commencer la partie
     */
    public void notifierProchainJoueur() {
        //Vérifier pour échec et mat ou match nul
        Collection<Move> moves = gameData.getAllLegalMoves(tourA.get());

        if (moves.isEmpty()) {
            if (gameData.getBoardMap().isPieceAttaquer(gameData.getRoi(tourA.get()))) {
                if (tourA.get() == Colour.NOIR) {
                    resultatListener.accept(Resultat.BLANC_GAGNE);
                } else {
                    resultatListener.accept(Resultat.NOIR_GAGNE);
                }
            } else {
                resultatListener.accept(Resultat.EGALITE);
            }
        } else {
            //Si la partie n'est pas fini notifier prochain player
            status.set(tourA.get() == Colour.BLANC ? Status.ATTENTE_BLANC : Status.ATTENTE_NOIR);
            joueurs.get(tourA.get()).getMouvement(this::jouer, tourA.get()); //Demander au player de bouger
        }
    }

    public void setResultatListener(Consumer<Resultat> resultatListener) {
        this.resultatListener = resultatListener;
    }

    /**
     * Appelé par le callback de player.getMouvement()
     *
     * @param move le moves à jouer
     */
    private void jouer(@NotNull Move move) {
        move.appliquer(gameData); //Jouer le moves
        moves.push(move); //Ajouter à la liste

        gameData.notifyListenerOfChange(); //Notifier changement

        changerLeTour();

        status.set(Status.INACTIF);
    }

    private void changerLeTour() {
        tourA.set(tourA.getValue() == Colour.BLANC ? Colour.NOIR : Colour.BLANC); //Changer le tour
    }

    /**
     * @param tour le nombre de moves à défaire
     */
    public void undo(int tour) {
        for (int i = 0; i < tour; i++) {
            moves.pop().undo(gameData);
            gameData.notifyListenerOfChange();
            changerLeTour();
        }

        status.set(Status.INACTIF);
    }

    public GameData getGameData() {
        return gameData;
    }

    @NotNull
    public EnumMap<Colour, Player> getJoueurs() {
        return joueurs;
    }

    ReadOnlyObjectProperty<Colour> tourAProperty() {
        return tourA.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return status.getReadOnlyProperty();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(tourA.get());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tourA = new ReadOnlyObjectWrapper<>((Colour) in.readObject());
        status = new ReadOnlyObjectWrapper<>(Status.INACTIF);
    }
}