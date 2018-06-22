package model.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import model.pieces.*

import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Represents a mapping of pieces and their position on the board.
 * Uses [BiMap] from Google Guava to allow quick access in both directions (find position of piece or find piece at position)
 */
class BoardMap : Serializable {
    private val board: BiMap<Position, Piece> = HashBiMap.create(32)

    fun getPiece(position: Position): Piece? {
        return board[position]
    }

    fun getPosition(piece: Piece): Position? {
        return board.inverse()[piece]
    }

    /**
     * @return the piece that was at this position before the new piece was added
     */
    fun add(position: Position, piece: Piece): Piece? {
        return board.put(position, piece)
    }

    /**
     * @param piece the piece that was at the destination before the piece was moved
     */
    @Synchronized
    fun movePiece(destination: Position, piece: Piece) {
        board.forcePut(destination, piece)
    }

    @Synchronized
    fun removePiece(position: Position): Piece {
        return board.remove(position) ?: throw IllegalArgumentException("No piece at: $position")
    }

    fun iteratePieces(): Set<Piece> {
        return board.values
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("[")

        for (piece in iteratePieces()) {
            stringBuilder.append(getPosition(piece)).append(" ").append(piece).append(", ")
        }

        stringBuilder.append("]")

        return stringBuilder.toString()
    }


    //Do not delete contains synchronized keyword
    @Synchronized
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    companion object {

        /**
         * @return a new board map with the initial (starting) layout of pieces
         */
        fun createStartingBoard(): BoardMap {
            val boardMap = BoardMap()
            boardMap.add(Position(7, 0), Rook(Colour.WHITE))
            boardMap.add(Position(7, 1), Knight(Colour.WHITE))
            boardMap.add(Position(7, 2), Bishop(Colour.WHITE))
            boardMap.add(Position(7, 3), Queen(Colour.WHITE))
            boardMap.add(Position(7, 4), King(Colour.WHITE))
            boardMap.add(Position(7, 5), Bishop(Colour.WHITE))
            boardMap.add(Position(7, 6), Knight(Colour.WHITE))
            boardMap.add(Position(7, 7), Rook(Colour.WHITE))

            boardMap.add(Position(6, 0), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 1), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 2), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 3), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 4), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 5), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 6), Pawn(Colour.WHITE))
            boardMap.add(Position(6, 7), Pawn(Colour.WHITE))

            boardMap.add(Position(1, 0), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 1), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 2), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 3), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 4), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 5), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 6), Pawn(Colour.BLACK))
            boardMap.add(Position(1, 7), Pawn(Colour.BLACK))

            boardMap.add(Position(0, 0), Rook(Colour.BLACK))
            boardMap.add(Position(0, 1), Knight(Colour.BLACK))
            boardMap.add(Position(0, 2), Bishop(Colour.BLACK))
            boardMap.add(Position(0, 3), Queen(Colour.BLACK))
            boardMap.add(Position(0, 4), King(Colour.BLACK))
            boardMap.add(Position(0, 5), Bishop(Colour.BLACK))
            boardMap.add(Position(0, 6), Knight(Colour.BLACK))
            boardMap.add(Position(0, 7), Rook(Colour.BLACK))

            return boardMap
        }
    }
}
