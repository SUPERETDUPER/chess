package model.pieces

import model.GameData
import model.moves.EnPassantMove
import model.moves.Move
import model.moves.PromotionMove
import model.util.BoardMap
import model.util.Colour
import model.util.Offset
import model.util.Position
import java.util.*

class Pawn(colour: Colour) : Piece(colour) {
    private val rightAttack = if (colour == Colour.WHITE) Offset.TOP_LEFT else Offset.BOTTOM_LEFT
    private val leftAttack = if (colour == Colour.WHITE) Offset.TOP_RIGHT else Offset.BOTTOM_RIGHT
    private val forward = if (colour == Colour.WHITE) Offset.UP else Offset.DOWN
    val backward = if (colour == Colour.WHITE) Offset.DOWN else Offset.UP
    private val forwardByTwo = Offset(if (Colour.WHITE == colour) -2 else 2, 0)

    private val startRow = if (colour == Colour.WHITE) Position.LIMIT - 2 else 1

    /**
     * null when not promoted and not-null when pawn was promoted
     */
    private var promotedQueen: Piece? = null

    override val unicodeBlack: Int
        get() = if (promotedQueen != null) promotedQueen!!.unicodeBlack else 9823

    override val unicodeWhite: Int
        get() = if (promotedQueen != null) promotedQueen!!.unicodeWhite else 9817

    override val unsignedValue: Int
        get() = if (promotedQueen != null) promotedQueen!!.unsignedValue else 1

    override val name: String = "Pawn"

    override fun convertDestinationToMove(board: BoardMap, current: Position, destination: Position): Move {
        if (promotedQueen != null) return promotedQueen!!.convertDestinationToMove(board, current, destination)

        //Check for promotion if on last row
        if (destination.row == 0 || destination.row == Position.LIMIT - 1)
            return PromotionMove(current, destination)
        //If moved sideways and there is no piece at destination must be en passant
        return if (current.column != destination.column && board.getPiece(destination) == null) EnPassantMove(current, destination) else super.convertDestinationToMove(board, current, destination)

    }

    override fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position> {
        //If promoted use queen to generate moves
        if (promotedQueen != null) return promotedQueen!!.generatePossibleDestinations(gameData, start)

        val positions = LinkedList<Position>()

        var forward = start.shift(forward)

        //If no one in spot in front can move else we are blocked
        //No need for .isValid check since when on edge piece is promoted to queen
        val notBlocked = gameData.board.getPiece(forward) == null
        if (notBlocked) positions.add(forward)

        //If not blocked and on start row we can move forward by two
        //No need for .isValid check since when on edge piece is promoted to queen
        if (notBlocked && start.row == startRow) {
            forward = start.shift(forwardByTwo)

            //Only move forward by two if square is empty
            if (gameData.board.getPiece(forward) == null) positions.add(forward)
        }

        //Try to eat pieces on the side (add en passant)
        forward = start.shift(rightAttack)
        if (canAttack(gameData, forward)) positions.add(forward)

        forward = start.shift(leftAttack)
        if (canAttack(gameData, forward)) positions.add(forward)

        return positions
    }

    private fun canAttack(gameData: GameData, destination: Position): Boolean {
        if (!destination.isValid) return false

        //If piece there return true if same color
        val piece = gameData.board.getPiece(destination)
        if (piece != null) {
            return piece.colour != colour//Can eat if piece exists and is other colour
        }

        //Get piece on the side
        val pieceOnSide = gameData.board.getPiece(destination.shift(backward))
        //If piece is null or not a pawn do nothing (or if same colour as me
        if (pieceOnSide !is Pawn || pieceOnSide.colour == colour) return false

        //Get last move
        val lastMove = gameData.pastMoves.last

        //If the last move affected the piece on the side and the piece on the side moved by two
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
