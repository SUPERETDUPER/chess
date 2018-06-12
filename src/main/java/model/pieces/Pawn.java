package model.pieces;

import model.moves.Move;
import model.moves.PromotionMove;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;
import java.util.LinkedList;

//TODO Implement promotion and en passant
public class Pawn extends Piece {
    private final Offset ATTAQUE_GAUCHE = getColour() == Colour.BLANC ? Offset.HAUT_GAUGHE : Offset.BAS_GAUCHE;
    private final Offset ATTAQUE_DROITE = getColour() == Colour.BLANC ? Offset.HAUT_DROIT : Offset.BAS_DROIT;
    private final Offset AVANCER = getColour() == Colour.BLANC ? Offset.HAUT : Offset.BAS;
    private final Offset SAUT = new Offset(Colour.BLANC == getColour() ? -2 : 2, 0);

    private int nombreDeMouvementsCompletes = 0;

    private Piece reine = null;

    public Pawn(Colour colour) {
        super(colour);
    }

    @Override
    Move convertir(BoardMap boardMap, Position debut, Position finale) {
        if (reine != null || (finale.getRangee() != 0 && finale.getRangee() != Position.LIMITE - 1))
            return super.convertir(boardMap, debut, finale);

        return new PromotionMove(debut, finale);
    }

    @Override
    Collection<Position> generatePosition(BoardMap boardMap, Position positionDebut) {
        if (reine != null) return reine.generatePosition(boardMap, positionDebut);

        Collection<Position> positions = new LinkedList<>();

        boolean blocked = false;

        Position fin = positionDebut.decaler(AVANCER);

        //Si une place de plus est valide est n'est pas promotion
        if (fin.isValid()) {

            //Si il y a personne on peut avancer
            if (boardMap.getPiece(fin) == null) {
                positions.add(fin);
            }
            //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && nombreDeMouvementsCompletes == 0) {
            fin = positionDebut.decaler(SAUT);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && boardMap.getPiece(fin) == null) {
                positions.add(fin);
            }
        }

        //On essaye de manger des pièces aux côtés
        fin = positionDebut.decaler(ATTAQUE_GAUCHE);
        if (canEat(boardMap, fin)) positions.add(fin);

        fin = positionDebut.decaler(ATTAQUE_DROITE);
        if (canEat(boardMap, fin)) positions.add(fin);

        return positions;
    }

    private boolean canEat(BoardMap boardMap, Position fin) {
        if (fin.isValid()) {
            Piece piece = boardMap.getPiece(fin);
            return piece != null && piece.getColour() != colour;
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
    public void notifyMoveCompleted(Move move) {
        if (move instanceof PromotionMove) {
            reine = new Queen(colour) {
                @Override
                public int hashCode() {
                    return Pawn.this.hashCode();
                }

                @Override
                public boolean equals(Object obj) {
                    return Pawn.this.equals(obj);
                }
            };
        }

        nombreDeMouvementsCompletes += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        if (move instanceof PromotionMove) {
            reine = null;
        }

        nombreDeMouvementsCompletes -= 1;
    }

    @Override
    String getNom() {
        return "Pawn";
    }
}
