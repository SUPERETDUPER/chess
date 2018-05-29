package modele.pieces;

import modele.moves.MouvementManger;
import modele.moves.MouvementNormal;
import modele.moves.Move;
import modele.plateau.Offset;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

//TODO Implement promotion and en passant
public class Pion extends Piece {
    private final int direction = getCouleur() == Couleur.BLANC ? -1 : 1;
    private final Offset ATTAQUE_GAUCHE = new Offset(direction, -1);
    private final Offset ATTAQUE_DROITE = new Offset(direction, 1);
    private final Offset SAUT = new Offset(2 * direction, 0);
    private final Offset AVANCER = new Offset(direction, 0);

    private boolean aSaute = false;

    public Pion(Couleur couleur) {
        super(couleur);
    }

    @Override
    int unicodeForWhite() {
        return 9817;
    }

    @Override
    int unicodeForBlack() {
        return 9823;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);

        return currentPosition.offset(ATTAQUE_GAUCHE).equals(position) ||
                currentPosition.offset(ATTAQUE_DROITE).equals(position);

    }

    @Override
    public Set<Move> generateAllMoves(Plateau plateau) {
        Set<Move> moves = new HashSet<>();

        Position currentPosition = plateau.getPosition(this);

        boolean blocked = false;

        Position fin = currentPosition.offset(AVANCER);

        //Si une place de plus est valide
        if (fin.isValid()) {
            //Si il y a personne on peut avancer
            if (plateau.getPiece(fin) == null) moves.add(new MouvementNormal(currentPosition, fin));
                //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && !aSaute) {
            fin = currentPosition.offset(SAUT);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && plateau.getPiece(fin) == null) {
                moves.add(new MouvementNormal(currentPosition, fin) {
                    @Override
                    public void appliquer(Plateau plateau) {
                        aSaute = true;
                        super.appliquer(plateau);
                    }

                    @Override
                    public void undo(Plateau plateau) {
                        aSaute = false;
                        super.undo(plateau);
                    }
                });
            }
        }

        //On essaye de manger des pièces aux côtés
        fin = currentPosition.offset(ATTAQUE_GAUCHE);

        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            if (piece != null && piece.getCouleur() != couleur) moves.add(new MouvementManger(currentPosition, fin));
        }

        fin = currentPosition.offset(ATTAQUE_DROITE);

        if (fin.isValid()) {
            Piece piece = plateau.getPiece(fin);
            if (piece != null && piece.getCouleur() != couleur) moves.add(new MouvementManger(currentPosition, fin));
        }

        return moves;
    }

    @Override
    public int getValeurPositive() {
        return 1;
    }
}
