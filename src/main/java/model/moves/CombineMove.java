package model.moves;

import model.GameData;
import model.util.Position;

/**
 * Un moves qui est un ensemble d'autre mouvements
 * La liste de mouvements est appliqué en ordre. Et la value est la somme
 */
public class CombineMove extends BaseMove {
    private final Move[] otherMoves;
    private Integer value;

    public CombineMove(Position debut, Position fin, Move[] otherMoves) {
        super(debut, fin);
        this.otherMoves = otherMoves;
    }

    @Override
    void applyToGame(GameData data) {
        super.applyToGame(data);

        for (Move move : otherMoves) {
            move.apply(data);
        }
    }

    @Override
    void undoToGame(GameData data) {
        for (int i = (otherMoves.length - 1); i >= 0; i--) {
            otherMoves[i].undo(data);
        }

        super.undoToGame(data);
    }

    @Override
    public int getValue() {
        if (value == null) {
            value = super.getValue();

            for (Move move : otherMoves) {
                value += move.getValue();
            }
        }

        return value;
    }
}
