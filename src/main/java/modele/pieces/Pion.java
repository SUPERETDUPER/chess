package modele.pieces;

import modele.mouvement.*;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.HashSet;
import java.util.Set;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private final Offset ATTAQUE_GAUCHE = getCouleur() == Couleur.BLANC ? Offset.HAUT_GAUGHE : Offset.BAS_GAUCHE;
    private final Offset ATTAQUE_DROITE = getCouleur() == Couleur.BLANC ? Offset.HAUT_DROIT : Offset.BAS_DROIT;
    private final Offset AVANCER = getCouleur() == Couleur.BLANC ? Offset.HAUT_CENTRE : Offset.BAS_CENTRE;
    private final Offset SAUT = new Offset(Couleur.BLANC == getCouleur() ? -2 : 2, 0);

    private int nombreDeMouvementsCompletes = 0;

    private Piece reine = null;

    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        if (reine != null) return reine.generateAllMoves(plateau);

        Set<Mouvement> mouvements = new HashSet<>();

        Position currentPosition = plateau.getPosition(this);

        boolean blocked = false;

        Position fin = currentPosition.decaler(AVANCER);

        //Si une place de plus est valide est n'est pas promotion
        if (fin.isValid()) {

            //Si il y a personne on peut avancer
            if (plateau.getPiece(fin) == null) {
                if (fin.getRangee() == 0 || fin.getRangee() == Position.LIMITE - 1) {
                    mouvements.add(new MouvementPromotion(this, fin));
                } else {
                    mouvements.add(new MouvementBouger(this, fin));
                }
            }
            //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && nombreDeMouvementsCompletes == 0) {
            fin = currentPosition.decaler(SAUT);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && plateau.getPiece(fin) == null) {
                mouvements.add(new MouvementBouger(this, fin));
            }
        }

        //On essaye de manger des pièces aux côtés
        checkCanEat(plateau, mouvements, currentPosition.decaler(ATTAQUE_GAUCHE));
        checkCanEat(plateau, mouvements, currentPosition.decaler(ATTAQUE_DROITE));

        return mouvements;
    }

    private void checkCanEat(Plateau plateau, Set<Mouvement> mouvements, Position fin) {
        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            if (piece != null && piece.getCouleur() != couleur) {
                if (fin.getRangee() == 0 || fin.getRangee() == Position.LIMITE - 1) {
                    mouvements.add(new MouvementPromotionManger(this, fin));
                } else {
                    mouvements.add(new MouvementManger(this, fin));
                }
            }
        }
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        if (reine != null) return reine.attaquePosition(plateau, position);

        Position currentPosition = plateau.getPosition(this);

        return currentPosition.decaler(ATTAQUE_GAUCHE).equals(position) ||
                currentPosition.decaler(ATTAQUE_DROITE).equals(position);
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
        if (mouvement instanceof MouvementPromotion || mouvement instanceof MouvementPromotionManger) {
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
        if (mouvement instanceof MouvementPromotion || mouvement instanceof MouvementPromotionManger) {
            reine = null;
        }

        nombreDeMouvementsCompletes -= 1;
    }

    @Override
    String getNom() {
        return "Pion";
    }
}
