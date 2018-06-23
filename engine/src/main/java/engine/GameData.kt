package engine

import engine.moves.Move
import engine.pieces.King
import engine.pieces.Piece
import engine.util.Colour
import engine.util.PieceMap
import engine.util.Position
import engine.util.switch
import java.io.Serializable
import java.util.*

/**
 * Represents the pieceMap state (pieceMap's positions) and the pieces that were eaten
 */
class GameData(val pieceMap: PieceMap) : Serializable {

    private val eatenPieces = EnumMap<Colour, Stack<Piece>>(Colour::class.java)

    /**
     * A list of completed moves
     */
    internal val pastMoves = LinkedList<Move>()

    /**
     * The kings for each player.
     */
    private val kings = EnumMap<Colour, King>(Colour::class.java)

    init {

        for (colour in Colour.values()) {
            eatenPieces[colour] = Stack()
        }

        for ((_, piece) in pieceMap) {
            if (piece is King) {
                if (kings.containsKey(piece.colour))
                    throw RuntimeException("There are two kings for the same player")

                kings[piece.colour] = piece
            }
        }
    }

    /**
     * @return the king of this colour
     */
    internal fun getKing(colour: Colour): King {
        return kings[colour]!!
    }

    /**
     * @param colour will not allow any moves that throw this colour's king in check
     * @return a collection of possible legal moves that can be player
     */
    internal fun getPossibleLegalMoves(colour: Colour): Collection<Move> {
        return filterOnlyLegal(getAllPossibleMoves(colour), colour)
    }

    /**
     * @param moves        a collection of legal and illegal moves
     * @param verifierPour legal for who (will remove all moves that throw this colour's king in check)
     * @return a filter collection containing only legal moves
     */
    fun filterOnlyLegal(moves: Collection<Move>, verifierPour: Colour): Collection<Move> {
        val legalMoves = ArrayList<Move>()

        //For each move apply it and check if the king is in check
        for (move in moves) {
            move.apply(this)

            if (!isPieceAttacked(kings[verifierPour]!!)) {
                legalMoves.add(move)
            }

            move.undo(this)
        }

        return legalMoves
    }

    /**
     * @return true if the pieceMap is being attacked by another pieceMap
     */
    internal fun isPieceAttacked(piece: Piece): Boolean {
        return isPositionAttacked(this.pieceMap[piece], switch(piece.colour))
    }

    fun isPositionAttacked(position: Position?, byWho: Colour): Boolean {
        for ((attackersPosition, attacker) in pieceMap) {
            //If pieceMap opposite color check if attacks position
            if (attacker.colour == byWho && attacker.isAttackingPosition(this, position!!, attackersPosition)) {
                return true
            }
        }

        return false
    }

    private fun getAllPossibleMoves(colour: Colour): Set<Move> {
        val moves = HashSet<Move>()

        for ((position, piece) in pieceMap) {
            if (piece.colour == colour) {
                moves.addAll(piece.generatePossibleMoves(this, position))
            }
        }
        return moves
    }

    /**
     * @return the stack of eaten pieces for this colour
     */
    fun getEatenPieces(colour: Colour): Stack<Piece> {
        return eatenPieces[colour]!!
    }
}