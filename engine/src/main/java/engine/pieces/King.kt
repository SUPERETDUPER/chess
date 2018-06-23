package engine.pieces

import engine.GameData
import engine.moves.BaseMove
import engine.moves.CastlingMove
import engine.moves.Move
import engine.util.*

internal class King(colour: Colour) : PositionPiece(colour) {
    /**
     * The number of moves that have been applied to this pieceMap. Used to know if the pieceMap has moved
     */
    private var numberOfAppliedMoves = 0

    override val unicodeWhite: Int = 9812

    override val unicodeBlack: Int = 9818

    override val offsets: Array<Position> = arrayOf(SHIFT_TOP_LEFT, SHIFT_UP, SHIFT_TOP_RIGHT, SHIFT_LEFT, SHIFT_RIGHT, SHIFT_BOTTOM_LEFT, SHIFT_DOWN, SHIFT_BOTTOM_RIGHT)

    /**
     * Large value to show that the King is the most valuable and should not be eaten
     */
    override val unsignedValue: Int = 1000

    override val name: String = "King"

    override fun generatePossibleDestinations(gameData: GameData, start: Position): MutableCollection<Position> {
        val positions: MutableCollection<Position> = super.generatePossibleDestinations(gameData, start)

        //If not in check and has not moved
        if (!gameData.isPositionAttacked(start, switch(colour)) && numberOfAppliedMoves == 0) {
            val castleShift = Position(0, 2)
            val destinationShort = start + castleShift
            if (canCastleShort(gameData, start, destinationShort)) positions.add(destinationShort)

            val destinationLong = start - castleShift
            if (canCastleLong(gameData, start, destinationLong)) positions.add(destinationLong)
        }

        return positions
    }

    private fun canCastleLong(gameData: GameData, kingStart: Position, kingEnd: Position): Boolean {
        val rook = gameData.pieceMap[kingEnd + Position(0, -2)]
        val rookDestination = kingStart + SHIFT_LEFT

        return rook is Rook && //There is a rook
                rook.numberOfAppliedMoves == 0 && //The rook did not move
                rookDestination !in gameData.pieceMap && //No piece where rook is going
                kingEnd !in gameData.pieceMap && //No piece where king is going
                (kingEnd + SHIFT_LEFT) !in gameData.pieceMap && //No piece where rook is passing through
                !gameData.isPositionAttacked(rookDestination, switch(colour)) //King not going through check
    }

    private fun canCastleShort(gameData: GameData, kingStart: Position, kingEnd: Position): Boolean {
        val rook = gameData.pieceMap[kingEnd + SHIFT_RIGHT]
        val rookDestination = kingStart + SHIFT_RIGHT

        return rook is Rook && //There is a rook
                rook.numberOfAppliedMoves == 0 && //The rook did not move
                rookDestination !in gameData.pieceMap && //No piece at the rook's destination
                kingEnd !in gameData.pieceMap && //No piece at the kings destination
                !gameData.isPositionAttacked(rookDestination, switch(colour)) //King not going through check
    }

    override fun convertDestinationToMove(piece: PieceMap, current: Position, destination: Position): Move {
        //Add catch to convert castling to CastlingMove
        //Short castling
        if (current.column - destination.column == -2)
            return CastlingMove(current, destination, BaseMove(destination + SHIFT_RIGHT, destination + SHIFT_LEFT))
        //Long castling
        if (current.column - destination.column == 2)
            return CastlingMove(current, destination, BaseMove(destination + Position(0, -2), destination + SHIFT_RIGHT))
        return super.convertDestinationToMove(piece, current, destination)
    }

    override fun isAttackingPosition(gameData: GameData, attackingPosition: Position, attackersPosition: Position): Boolean {
        //Do not remove, prevents infinite loop by using super generate possible destinations (without castling)
        return super.generatePossibleDestinations(gameData, attackersPosition).contains(attackingPosition)
    }

    override fun notifyMoveComplete(move: Move) {
        numberOfAppliedMoves += 1
    }

    override fun notifyMoveUndo(move: Move) {
        numberOfAppliedMoves -= 1
    }
}