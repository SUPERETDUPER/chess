package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.moves.MouvementManger;
import modele.moves.MouvementNormal;
import modele.plateau.Offset;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private final Offset ATTAQUE_GAUCHE = getCouleur() == Couleur.BLANC ? Offset.HAUT_GAUGHE : Offset.BAS_GAUCHE;
    private final Offset ATTAQUE_DROITE = getCouleur() == Couleur.BLANC ? Offset.HAUT_DROIT : Offset.BAS_DROIT;
    private final Offset AVANCER = getCouleur() == Couleur.BLANC ? Offset.HAUT_CENTRE : Offset.BAS_CENTRE;
    private final Offset SAUT = new Offset(Couleur.BLANC == getCouleur() ? -2 : 2, 0);

    private int nombreDeMouvementsCompletes = 0;

    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        Set<Mouvement> mouvements = new HashSet<>();

        Position currentPosition = plateau.getPosition(this);

        boolean blocked = false;

        Position fin = currentPosition.decaler(AVANCER);

        //Si une place de plus est valide est n'est pas promotion
        if (fin.isValid() && fin.getRangee() != 0 && fin.getRangee() != Position.LIMITE - 1) {
            //Si il y a personne on peut avancer
            if (plateau.getPiece(fin) == null) {
                mouvements.add(new MouvementNormal(this, fin));
            }
            //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && nombreDeMouvementsCompletes == 0) {
            fin = currentPosition.decaler(SAUT);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && plateau.getPiece(fin) == null) {
                mouvements.add(new MouvementNormal(this, fin));
            }
        }

        //On essaye de manger des pièces aux côtés
        fin = currentPosition.decaler(ATTAQUE_GAUCHE);

        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            if (piece != null && piece.getCouleur() != couleur)
                mouvements.add(new MouvementManger(this, fin));
        }

        fin = currentPosition.decaler(ATTAQUE_DROITE);

        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            if (piece != null && piece.getCouleur() != couleur)
                mouvements.add(new MouvementManger(this, fin));
        }

        return mouvements;
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);

        return currentPosition.decaler(ATTAQUE_GAUCHE).equals(position) ||
                currentPosition.decaler(ATTAQUE_DROITE).equals(position);
    }

    @Override
    int unicodeForBlack() {
        return 9823;
    }

    @Override
    int unicodeForWhite() {
        return 9817;
    }

    @Override
    public int getValeurPositive() {
        return 1;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
        nombreDeMouvementsCompletes += 1;
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
        nombreDeMouvementsCompletes -= 1;
    }
}
