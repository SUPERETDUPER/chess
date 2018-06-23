package ui.game.layout

import engine.util.Colour
import engine.util.Position
import javafx.beans.binding.NumberBinding
import javafx.beans.value.ObservableNumberValue

/**
 * A position in the graveyard
 *
 * @property positionIndex The position in the stack of the piece (one more than the past)
 */
data class GraveyardGraphicPosition internal constructor(private val positionIndex: Int, private val layoutCalculator: LayoutCalculator, private val colour: Colour) : GraphicPosition {

    //True if we should use the left column (depends on the colour)
    override val x: ObservableNumberValue = if (colour == Colour.BLACK && positionIndex < Position.LIMIT || colour == Colour.WHITE && positionIndex >= Position.LIMIT) {
            layoutCalculator.getGraveyardXOffset(colour)
        } else {
            layoutCalculator.componentSize.add(layoutCalculator.getGraveyardXOffset(colour))
        }

    override val y: NumberBinding = layoutCalculator.componentSize
                .multiply(if (positionIndex < Position.LIMIT) positionIndex else positionIndex - Position.LIMIT)
}
