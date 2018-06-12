package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementPromotion;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.Collection;
import java.util.LinkedList;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private final Offset ATTAQUE_GAUCHE = getCouleur() == Couleur.BLANC ? Offset.HAUT_GAUGHE : Offset.BAS_GAUCHE;
    private final Offset ATTAQUE_DROITE = getCouleur() == Couleur.BLANC ? Offset.HAUT_DROIT : Offset.BAS_DROIT;
    private final Offset AVANCER = getCouleur() == Couleur.BLANC ? Offset.HAUT : Offset.BAS;
    private final Offset SAUT = new Offset(Couleur.BLANC == getCouleur() ? -2 : 2, 0);

    private int nombreDeMouvementsCompletes = 0;

    private Piece reine = null;

    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    Mouvement convertir(Plateau plateau, Position position) {
        if (reine != null || (position.getRangee() != 0 && position.getRangee() != Position.LIMITE - 1))
            return super.convertir(plateau, position);

        return new MouvementPromotion(this, position);
    }

    @Override
    Collection<Position> generatePosition(Plateau plateau) {
        if (reine != null) return reine.generatePosition(plateau);

        Collection<Position> positions = new LinkedList<>();

        Position currentPosition = plateau.getPosition(this);

        boolean blocked = false;

        Position fin = currentPosition.decaler(AVANCER);

        //Si une place de plus est valide est n'est pas promotion
        if (fin.isValid()) {

            //Si il y a personne on peut avancer
            if (plateau.getPiece(fin) == null) {
                positions.add(fin);
            }
            //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && nombreDeMouvementsCompletes == 0) {
            fin = currentPosition.decaler(SAUT);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && plateau.getPiece(fin) == null) {
                positions.add(fin);
            }
        }

        //On essaye de manger des pièces aux côtés
        fin = currentPosition.decaler(ATTAQUE_GAUCHE);
        if (canEat(plateau, fin)) positions.add(fin);

        fin = currentPosition.decaler(ATTAQUE_DROITE);
        if (canEat(plateau, fin)) positions.add(fin);

        return positions;
    }

    private boolean canEat(Plateau plateau, Position fin) {
        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            return piece != null && piece.getCouleur() != couleur;
        }

        return false;
    }

    @Override
    int unicodeForBlack() {
        if (reine != null) return reine.unicodeForBlack();

        return 9823;
    }

    @Override
    int unicodeForWhite() {
        if (reine != null) return reine.unicodeForWhite();
        return 9817;
    }

    @Override
    public int getValeurPositive() {
        if (reine != null) return reine.getValeurPositive();

        return 1;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
        if (mouvement instanceof MouvementPromotion) {
            reine = new Reine(couleur) {
                @Override
                public int hashCode() {
                    return Pion.this.hashCode();
                }

                @Override
                public boolean equals(Object obj) {
                    return Pion.this.equals(obj);
                }
            };
        }

        nombreDeMouvementsCompletes += 1;
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
        if (mouvement instanceof MouvementPromotion) {
            reine = null;
        }

        nombreDeMouvementsCompletes -= 1;
    }

    @Override
    String getNom() {
        return "Pion";
    }
}
