package model.pieces;

import model.GameData;
import model.moves.BaseMove;
import model.moves.CastlingMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class King extends OffsetPiece {
    private static final Offset[] OFFSETS = {
            Offset.TOP_LEFT,
            Offset.UP,
            Offset.TOP_RIGHT,
            Offset.LEFT,
            Offset.RIGHT,
            Offset.BOTTOM_LEFT,
            Offset.DOWN,
            Offset.BOTTOM_RIGHT
    };

    /**
     * The number of moves that have been applied to this piece. Used to know if the piece has moved
     */
    private int numberOfAppliedMoves = 0;

    public King(Colour colour) {
        super(colour);
    }

    @Override
    int getUnicodeWhite() {
        return 9812;
    }

    @Override
    int getUnicodeBlack() {
        return 9818;
    }

    @NotNull
    @Override
    Offset[] getOffsets() {
        return OFFSETS;
    }

    /**
     * Large value to show that the King is the most valuable and should not be eaten
     */
    @Override
    public int getUnsignedValue() {
        return 1000;
    }

    @Override
    Collection<Position> generatePossibleDestinations(@NotNull GameData gameData, @NotNull Position start) {
        Collection<Position> positions = super.generatePossibleDestinations(gameData, start);

        //If not in check
        if (!gameData.isPositionAttacked(start, colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE) &&
                numberOfAppliedMoves == 0) {
            Position destinationShort = start.shift(new Offset(0, 2));
            if (canCastleShort(gameData, start, destinationShort)) positions.add(destinationShort);

            Position destinationLong = start.shift(new Offset(0, -2));
            if (canCastleLong(gameData, start, destinationLong)) positions.add(destinationLong);
        }

        return positions;
    }

    private boolean canCastleLong(GameData gameData, Position start, @NotNull Position end) {
        Position positionLeft = start.shift(Offset.LEFT);
        Position rookPosition = start.shift(new Offset(0, -4));
        Piece rook = gameData.getBoard().getPiece(rookPosition);

        //Piece at rook's position is a rook and has not moved
        if (!(rook instanceof Rook) || ((Rook) rook).hasMoved()) return false;

        if (gameData.getBoard().getPiece(positionLeft) != null ||
                gameData.getBoard().getPiece(end) != null ||
                gameData.getBoard().getPiece(end.shift(Offset.LEFT)) != null) return false;

        return !gameData.isPositionAttacked(positionLeft, colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE);
    }

    private boolean canCastleShort(GameData gameData, Position start, Position end) {
        Position positionRight = start.shift(Offset.RIGHT);
        Position rookPosition = start.shift(new Offset(0, 3));
        Piece rook = gameData.getBoard().getPiece(rookPosition);

        //Piece at rook's position is a rook and has not moved
        if (!(rook instanceof Rook) || ((Rook) rook).hasMoved()) return false;

        //Nothing at the position to the right or at the end
        if (gameData.getBoard().getPiece(positionRight) != null || gameData.getBoard().getPiece(end) != null)
            return false;

        //Not going through check
        return !gameData.isPositionAttacked(positionRight, colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE);
    }

    @Override
    Move convertDestinationToMove(BoardMap board, @NotNull Position current, @NotNull Position destination) {
        //Add catch to convert castling to CastlingMove
        if (current.getColumn() - destination.getColumn() == -2)
            return new CastlingMove(current, destination, new Move[]{
                    new BaseMove(destination.shift(Offset.RIGHT), destination.shift(Offset.LEFT))
            });
        if (current.getColumn() - destination.getColumn() == 2)
            return new CastlingMove(current, destination, new Move[]{
                    new BaseMove(destination.shift(new Offset(0, -2)), destination.shift(Offset.RIGHT))
            });

        return super.convertDestinationToMove(board, current, destination);
    }

    @Override
    public boolean isAttackingPosition(GameData gameData, Position position) {
        Collection<Position> positions = super.generatePossibleDestinations(gameData, position);
        return positions.contains(position);
    }

    @Override
    public void notifyMoveComplete(Move move) {
        numberOfAppliedMoves += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        numberOfAppliedMoves -= 1;
    }

    @NotNull
    @Override
    String getName() {
        return "King";
    }
}