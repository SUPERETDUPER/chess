package model.pieces

import model.GameData
import model.moves.BaseMove
import model.moves.CastlingMove
import model.moves.Move
import model.util.BoardMap
import model.util.Colour
import model.util.Offset
import model.util.Position

internal class King(colour: Colour) : OffsetPiece(colour) {


    /**
     * The number of moves that have been applied to this piece. Used to know if the piece has moved
     */
    private var numberOfAppliedMoves = 0

    override val unicodeWhite: Int
        get() = 9812

    override val unicodeBlack: Int
        get() = 9818

    override val offsets: Array<Offset>
        get() = OFFSETS

    /**
     * Large value to show that the King is the most valuable and should not be eaten
     */
    override val unsignedValue: Int
        get() = 1000

    override val name: String
        get() = "King"


    override fun generatePossibleDestinations(gameData: GameData, start: Position): MutableCollection<Position> {
        val positions: MutableCollection<Position> = super.generatePossibleDestinations(gameData, start)

        //If not in check
        if (!gameData.isPositionAttacked(start, if (colour == Colour.WHITE) Colour.BLACK else Colour.WHITE) && numberOfAppliedMoves == 0) {
            val destinationShort = start.shift(Offset(0, 2))
            if (canCastleShort(gameData, start, destinationShort)) positions.add(destinationShort)

            val destinationLong = start.shift(Offset(0, -2))
            if (canCastleLong(gameData, start, destinationLong)) positions.add(destinationLong)
        }

        return positions
    }

    private fun canCastleLong(gameData: GameData, start: Position, end: Position): Boolean {
        val positionLeft = start.shift(Offset.LEFT)
        val rookPosition = start.shift(Offset(0, -4))
        val rook = gameData.board.getPiece(rookPosition)

        //Piece at rook's position is a rook and has not moved
        if (rook !is Rook || rook.hasMoved()) return false

        return if (gameData.board.getPiece(positionLeft) != null ||
                gameData.board.getPiece(end) != null ||
                gameData.board.getPiece(end.shift(Offset.LEFT)) != null) false else !gameData.isPositionAttacked(positionLeft, if (colour == Colour.WHITE) Colour.BLACK else Colour.WHITE)

    }

    private fun canCastleShort(gameData: GameData, start: Position, end: Position): Boolean {
        val positionRight = start.shift(Offset.RIGHT)
        val rookPosition = start.shift(Offset(0, 3))
        val rook = gameData.board.getPiece(rookPosition)

        //Piece at rook's position is a rook and has not moved
        if (rook !is Rook || rook.hasMoved()) return false

        //Nothing at the position to the right or at the end
        return if (gameData.board.getPiece(positionRight) != null || gameData.board.getPiece(end) != null) false else !gameData.isPositionAttacked(positionRight, if (colour == Colour.WHITE) Colour.BLACK else Colour.WHITE)

        //Not going through check
    }

    override fun convertDestinationToMove(board: BoardMap, current: Position, destination: Position): Move {
        //Add catch to convert castling to CastlingMove
        if (current.column - destination.column == -2)
            return CastlingMove(current, destination, arrayOf(BaseMove(destination.shift(Offset.RIGHT), destination.shift(Offset.LEFT))))
        return if (current.column - destination.column == 2) CastlingMove(current, destination, arrayOf(BaseMove(destination.shift(Offset(0, -2)), destination.shift(Offset.RIGHT)))) else super.convertDestinationToMove(board, current, destination)

    }

    override fun isAttackingPosition(gameData: GameData, position: Position): Boolean {
        val positions = super.generatePossibleDestinations(gameData, gameData.board.getPosition(this)!!)
        return positions.contains(position)
    }

    override fun notifyMoveComplete(move: Move) {
        numberOfAppliedMoves += 1
    }

    override fun notifyMoveUndo(move: Move) {
        numberOfAppliedMoves -= 1
    }

    companion object {
        private val OFFSETS = arrayOf(Offset.TOP_LEFT, Offset.UP, Offset.TOP_RIGHT, Offset.LEFT, Offset.RIGHT, Offset.BOTTOM_LEFT, Offset.DOWN, Offset.BOTTOM_RIGHT)
    }
}