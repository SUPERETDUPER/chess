package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.moves.MouvementNormal;
import modele.moves.MouvementNotifyDecorator;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.Set;

public class Pion extends Piece {
    private final PionSimple pion;
    private final Reine reine;
    private Piece piece;

    private boolean switched = false;

    public Pion(Couleur couleur) {
        super(couleur);
        pion = new PionSimple(couleur);
        reine = new Reine(couleur);
        piece = pion;
    }

    @Override
    int unicodeForWhite() {
        return piece.unicodeForWhite();
    }

    @Override
    int unicodeForBlack() {
        return piece.unicodeForBlack();
    }

    @Override
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        Set<Mouvement> mouvements = piece.generateAllMoves(plateau);

        if (piece == pion) {
            Position positionFinale = plateau.getPosition(piece).decaler(pion.AVANCER);

            if (positionFinale.getRangee() == 0 || positionFinale.getRangee() == Position.LIMITE - 1) {
                mouvements.add(new MouvementNotifyDecorator<Boolean>(
                        new MouvementNormal(piece, positionFinale) {
                            @Override
                            public int getValeur() {
                                return reine.getValeur() - pion.getValeur();
                            }
                        },
                        () -> {
                            if (piece == pion) {
                                piece = reine;
                                return true;
                            }
                            return false;
                        },
                        aBoolean -> {
                            if (aBoolean) {
                                piece = pion;
                            }
                        }
                ));
            }
        }

        return mouvements;
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        return piece.attaquePosition(plateau, position);
    }

    @Override
    public int getValeurPositive() {
        return piece.getValeurPositive();
    }

    @Override
    public int hashCode() {
        return piece.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return piece.equals(obj);
    }
}
