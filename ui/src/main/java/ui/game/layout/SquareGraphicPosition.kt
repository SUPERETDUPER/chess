package ui.game.layout

import javafx.beans.binding.NumberBinding
import engine.util.Position

/**
 * The position of a square
 *
 * @property position         the square's position
 * @property layoutCalculator the height of the board
 */
data class SquareGraphicPosition internal constructor(val position: Position, private val layoutCalculator: LayoutCalculator) : GraphicPosition {

    override val x: NumberBinding = layoutCalculator.componentSize
                .multiply(position.column).add(layoutCalculator.boardXOffset)

    override val y: NumberBinding = layoutCalculator.componentSize.multiply(position.row)
}
