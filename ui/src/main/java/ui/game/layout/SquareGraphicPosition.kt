package ui.game.layout

import javafx.beans.binding.NumberBinding
import model.util.Position

/**
 * The position of a square
 */
class SquareGraphicPosition
/**
 * @param position         the square's position
 * @param layoutCalculator the height of the board
 */
internal constructor(val position: Position, private val layoutCalculator: LayoutCalculator) : GraphicPosition {

    override val x: NumberBinding
        get() = layoutCalculator.componentSize
                .multiply(position.column).add(layoutCalculator.boardXOffset)

    override val y: NumberBinding
        get() = layoutCalculator.componentSize
                .multiply(position.row)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return if (other !is SquareGraphicPosition) false else other.position == this.position
    }

    override fun toString(): String {
        return "Square: $position"
    }
}
