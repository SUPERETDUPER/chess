package engine.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import engine.pieces.*

import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Represents a mapping of pieces and their position on the pieceMap.
 * Uses [BiMap] from Google Guava to allow quick access in both directions (find position of pieceMap or find pieceMap at position)
 */
class PieceMap : Serializable, Iterable<Map.Entry<Position, Piece>> {
    private val board: BiMap<Position, Piece> = HashBiMap.create(32)

    operator fun get(position: Position): Piece? {
        return board[position]
    }

    operator fun get(piece: Piece): Position? {
        return board.inverse()[piece]
    }

    operator fun contains(position: Position): Boolean {
        return board.containsKey(position)
    }

    operator fun contains(piece: Piece): Boolean {
        return board.containsValue(piece)
    }

    /**
     * @return the pieceMap that was at this position before the new pieceMap was added
     */
    fun put(position: Position, piece: Piece): Piece? {
        return board.put(position, piece)
    }

    /**
     * @param piece the pieceMap that was at the destination before the pieceMap was moved
     */
    @Synchronized
    fun movePiece(destination: Position, piece: Piece) {
        board.forcePut(destination, piece)
    }

    @Synchronized
    fun removePiece(position: Position): Piece {
        return board.remove(position) ?: throw IllegalArgumentException("No pieceMap at: $position")
    }

    override fun iterator(): Iterator<Map.Entry<Position, Piece>> {
        return board.entries.iterator()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder().append("[")

        for (entry in board.entries) {
            stringBuilder.append(entry).append(", ")
        }

        return stringBuilder.append("]").toString()
    }


    //Do not delete contains synchronized keyword
    @Synchronized
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
    }

    companion object {

        /**
         * @return a new pieceMap map with the initial (starting) layout of pieces
         */
        fun createStartingBoard(): PieceMap {
            val boardMap = PieceMap()
            boardMap.put(Position(7, 0), Rook(Colour.WHITE))
            boardMap.put(Position(7, 1), Knight(Colour.WHITE))
            boardMap.put(Position(7, 2), Bishop(Colour.WHITE))
            boardMap.put(Position(7, 3), Queen(Colour.WHITE))
            boardMap.put(Position(7, 4), King(Colour.WHITE))
            boardMap.put(Position(7, 5), Bishop(Colour.WHITE))
            boardMap.put(Position(7, 6), Knight(Colour.WHITE))
            boardMap.put(Position(7, 7), Rook(Colour.WHITE))

            boardMap.put(Position(6, 0), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 1), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 2), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 3), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 4), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 5), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 6), Pawn(Colour.WHITE))
            boardMap.put(Position(6, 7), Pawn(Colour.WHITE))

            boardMap.put(Position(1, 0), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 1), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 2), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 3), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 4), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 5), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 6), Pawn(Colour.BLACK))
            boardMap.put(Position(1, 7), Pawn(Colour.BLACK))

            boardMap.put(Position(0, 0), Rook(Colour.BLACK))
            boardMap.put(Position(0, 1), Knight(Colour.BLACK))
            boardMap.put(Position(0, 2), Bishop(Colour.BLACK))
            boardMap.put(Position(0, 3), Queen(Colour.BLACK))
            boardMap.put(Position(0, 4), King(Colour.BLACK))
            boardMap.put(Position(0, 5), Bishop(Colour.BLACK))
            boardMap.put(Position(0, 6), Knight(Colour.BLACK))
            boardMap.put(Position(0, 7), Rook(Colour.BLACK))

            return boardMap
        }
    }
}
