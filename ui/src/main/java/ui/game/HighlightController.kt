package ui.game

import engine.moves.Move
import engine.util.Board
import engine.util.Position
import ui.game.components.SquarePane
import java.util.*

/**
 * Controls how the squares should be highlighted
 * When a piece is highlighted, the piece's square is made red and the possible moves more the piece can go is made blue
 *
 *
 * @property squares the list of squares at each position
 */
internal class HighlightController(private val squares: Board<SquarePane>) {

    /**
     * The list of possible moves for the highlighted piece
     * Empty if no piece is highlighted
     */
    private val possibleMoves = HashMap<Position, Move>()

    /**
     * The selected piece's position
     */
    private var selectedPosition: Position? = null

    val isSelected: Boolean
        get() = selectedPosition != null

    /**
     * Selects a square (with a piece on it) and highlights the possible moves for that piece to move to
     *
     * @param position      the selected position
     * @param possibleMoves a list of possible moves for that position
     */
    fun select(position: Position, possibleMoves: Collection<Move>) {
        eraseSelection()

        //Highlight position
        this.selectedPosition = position
        squares[position].setStyle(SquarePane.Style.RED)

        //For each move
        for (move in possibleMoves) {
            val end = move.end

            squares[end].setStyle(SquarePane.Style.BLUE) //Highlight end position
            this.possibleMoves[end] = move //Add to list
        }
    }

    /**
     * Erases the current selection (removes highlight)
     */
    fun eraseSelection() {
        //If nothing selected return
        if (selectedPosition == null) return

        //Remove highlight from selected position
        squares[selectedPosition!!].setStyle(SquarePane.Style.NORMAL)

        //Remove highlight from each option
        for (position in possibleMoves.keys) {
            squares[position].setStyle(SquarePane.Style.NORMAL)
        }

        //Clear list and selectedPosition
        possibleMoves.clear()
        selectedPosition = null
    }

    /**
     * @return true if the position is one of the possible moves (highlighted in blue)
     */
    fun isPossibleMove(position: Position): Boolean {
        return possibleMoves.containsKey(position)
    }

    /**
     * @return the move associated with the position
     */
    fun getPossibleMove(position: Position): Move? {
        return possibleMoves[position]
    }
}
