package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.Action;
import modele.moves.MoveAction;

import java.util.ArrayList;
import java.util.List;

public class Roi extends Piece {
    public Roi(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int unicodeForWhite() {
        return 9812;
    }

    @Override
    int unicodeForBlack() {
        return 9818;
    }

    @Override
    Action[] getPossibleActions(Board board) {
        List<Action> actions = new ArrayList<>();

        Position currentPose = board.getPosition(this);
        actions.add(new MoveAction(currentPose, currentPose.offset(-1, -1)));
        actions.add(new MoveAction(currentPose, currentPose.offset(-1, 0)));
        actions.add(new MoveAction(currentPose, currentPose.offset(-1, 1)));
        actions.add(new MoveAction(currentPose, currentPose.offset(0, -1)));
        actions.add(new MoveAction(currentPose, currentPose.offset(0, 1)));
        actions.add(new MoveAction(currentPose, currentPose.offset(1, -1)));
        actions.add(new MoveAction(currentPose, currentPose.offset(1, 0)));
        actions.add(new MoveAction(currentPose, currentPose.offset(1, 1)));

        return actions.toArray(new Action[0]);
    }
}
