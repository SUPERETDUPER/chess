package modele;

public class Modele {
    private final Jeu jeu;
    private final Chargeur chargeur;

    public Modele(Jeu jeu, Chargeur chargeur) {
        this.jeu = jeu;
        this.chargeur = chargeur;
    }

    public Chargeur getChargeur() {
        return chargeur;
    }

    public Jeu getJeu() {
        return jeu;
    }
}
