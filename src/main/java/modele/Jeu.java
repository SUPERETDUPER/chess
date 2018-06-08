package modele;

import javafx.beans.property.ReadOnlyObjectWrapper;
import modele.joueur.Joueur;
import modele.moves.Mouvement;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Classe qui supervise les joueurs et s'assure de respecter les tours
 */
public class Jeu implements Serializable {
    public enum Resultat {
        BLANC_GAGNE,
        NOIR_GAGNE,
        EGALITE
    }

    private final JeuData jeuData;

    @NotNull
    private EnumMap<Couleur, Joueur> joueurs;

    private ReadOnlyObjectWrapper<Couleur> tourA = new ReadOnlyObjectWrapper<>(Couleur.BLANC);

    private Consumer<Resultat> resultatListener;

    public Jeu(JeuData jeuData, @NotNull EnumMap<Couleur, Joueur> joueurs) {
        this.jeuData = jeuData;
        this.joueurs = joueurs;

        for (Joueur joueur : joueurs.values()) {
            joueur.initializeJeuData(jeuData);
        }
    }

    /**
     * Commencer la partie
     */
    public void commencer() {
        joueurs.get(tourA.get()).getMouvement(this::jouer, tourA.get());
    }

    public void setResultatListener(Consumer<Resultat> resultatListener) {
        this.resultatListener = resultatListener;
    }

    /**
     * Appelé par le callback de joueur.getMouvement()
     *
     * @param mouvement le mouvement à jouer
     */
    private void jouer(@NotNull Mouvement mouvement) {
        mouvement.appliquer(jeuData.getPlateau()); //Jouer le mouvement

        jeuData.notifyListenerOfChange(jeuData.getPlateau().getCopie());

        tourA.set(tourA.getValue() == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC); //Changer le tour

        //Vérifier pour échec et mat ou match nul
        Set<Mouvement> mouvements = jeuData.getAllLegalMoves(tourA.get());

        if (mouvements.isEmpty()) {
            if (jeuData.getPlateau().isPieceAttaquer(jeuData.getRoi(tourA.get()))) {
                if (tourA.get() == Couleur.NOIR) {
                    resultatListener.accept(Resultat.BLANC_GAGNE);
                } else {
                    resultatListener.accept(Resultat.NOIR_GAGNE);
                }
                System.out.println("Checkmate");
            } else {
                resultatListener.accept(Resultat.EGALITE);
                System.out.println("Stalemate");
            }
        } else {
            joueurs.get(tourA.get()).getMouvement(this::jouer, tourA.get()); //Notifier l'autre joueur qu'il peut joueur
        }
    }

    public JeuData getJeuData() {
        return jeuData;
    }

    @NotNull
    public EnumMap<Couleur, Joueur> getJoueurs() {
        return joueurs;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Consumer<Resultat> listener = resultatListener;
        resultatListener = null;

        Couleur tourAValue = this.tourA.get();
        this.tourA = null;

        out.defaultWriteObject();
        out.writeObject(tourAValue);

        this.tourA = new ReadOnlyObjectWrapper<>(tourAValue);
        resultatListener = listener;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tourA = new ReadOnlyObjectWrapper<>((Couleur) in.readObject());
    }

    ReadOnlyObjectWrapper<Couleur> tourAProperty() {
        return tourA;
    }
}