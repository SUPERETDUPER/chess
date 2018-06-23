package engine.pieces

import engine.GameData
import engine.moves.EnPassantMove
import engine.moves.Move
import engine.moves.PromotionMove
import engine.util.*
import java.util.*

class Pawn(colour: Colour) : Piece(colour) {
    private val rightAttack = if (colour == Colour.WHITE) SHIFT_TOP_LEFT else SHIFT_BOTTOM_LEFT
    private val leftAttack = if (colour == Colour.WHITE) SHIFT_TOP_RIGHT else SHIFT_BOTTOM_RIGHT
    private val forwardShift = if (colour == Colour.WHITE) SHIFT_UP else SHIFT_DOWN
    val backwardShift = if (colour == Colour.WHITE) SHIFT_DOWN else SHIFT_UP
    private val forwardByTwoShift = Position(if (Colour.WHITE == colour) -2 else 2, 0)

    private val startRow = if (colour == Colour.WHITE) LIMIT - 2 else 1
    private val promotionRow = if (colour == Colour.WHITE) 0 else LIMIT - 1

    /**
     * null when not promoted and not-null when pawn was promoted
     */
    private var promotedQueen: Piece? = null

    override val unicodeBlack: Int
        get() = promotedQueen?.unicodeBlack ?: 9823

    override val unicodeWhite: Int
        get() = promotedQueen?.unicodeWhite ?: 9817

    override val unsignedValue: Int
        get() = promotedQueen?.unsignedValue ?: 1

    override val name: String = "Pawn"

    override fun convertDestinationToMove(piece: PieceMap, current: Position, destination: Position): Move {
        promotedQueen?.let { return it.convertDestinationToMove(piece, current, destination) }

        //If destination is last row must be promotion
        if (destination.row == promotionRow) return PromotionMove(current, destination)
        //If moved sideways (changed column) and there is no pieceMap at destination must be en passant
        if (current.column != destination.column && destination !in piece) return EnPassantMove(current, destination)
        return super.convertDestinationToMove(piece, current, destination)
    }

    override fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position> {
        //If promoted use queen to generate moves
        promotedQueen?.let { return it.generatePossibleDestinations(gameData, start) }

        val positions = LinkedList<Position>()

        val forwardPosition = start + forwardShift

        //If spot in front is empty we can move
        //No need for .isValid check since when on edge pieceMap is promoted to queen
        if (forwardPosition !in gameData.pieceMap) {
            positions.add(forwardPosition)

            //If we are on the start row we can also move by two
            if (start.row == startRow) {
                val jump = start + forwardByTwoShift

                //Only move forward by two if square is empty
                if (jump !in gameData.pieceMap) positions.add(jump)
            }
        }

        //Try to eat pieces on the side (put en passant)
        val rightAttackPosition = start + rightAttack
        if (canAttack(gameData, rightAttackPosition)) positions.add(rightAttackPosition)

        val leftAttackPosition = start + leftAttack
        if (canAttack(gameData, leftAttackPosition)) positions.add(leftAttackPosition)

        return positions
    }

    private fun canAttack(gameData: GameData, destination: Position): Boolean {
        if (!destination.isValid) return false

        //If pieceMap there return true if different color
        val piece = gameData.pieceMap[destination]
        if (piece != null) {
            return piece.colour != colour  //Can eat if pieceMap exists and is other colour
        }

        //Check for en passant
        //Get pieceMap on the side
        val pieceOnSide = gameData.pieceMap[destination + backwardShift]
        //If pieceMap is null or not a pawn do nothing (or if same colour as me)
        if (pieceOnSide !is Pawn || pieceOnSide.colour == colour) return false

        //Get last move
        val lastMove = gameData.pastMoves.last

        //If the last move affected the pieceMap on the side and the pieceMap on the side moved by two
        return lastMove.piece === pieceOnSide && Math.abs(lastMove.start.row - lastMove.end.row) == 2
    }

    override fun notifyMoveComplete(move: Move) {
        if (move is PromotionMove) {
            promotedQueen = Queen(colour)
        }
    }

    override fun notifyMoveUndo(move: Move) {
        if (move is PromotionMove) {
            promotedQueen = null
        }
    }
}
