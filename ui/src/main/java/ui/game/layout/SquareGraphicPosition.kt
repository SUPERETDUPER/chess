package ui.game.layout

import javafx.beans.binding.NumberBinding
import engine.util.Position

/**
 * The position of a square
 *
 * @property position         the square's position
 * @property layoutCalculator the height of the pieceMap
 */
data class SquareGraphicPosition internal constructor(val position: Position, private val layoutCalculator: LayoutCalculator) : GraphicPosition {

    override val x: NumberBinding = layoutCalculator.componentSize
                .multiply(position.column).add(layoutCalculator.boardXPosition)

    override val y: NumberBinding = layoutCalculator.componentSize.multiply(position.row)
}
