package ui.game.layout

import engine.util.Colour
import engine.util.LIMIT
import javafx.beans.binding.NumberBinding
import javafx.beans.value.ObservableNumberValue

/**
 * A position in the graveyard
 *
 * @property positionIndex The position in the stack of the pieceMap (one more than the past)
 */
data class GraveyardGraphicPosition internal constructor(private val positionIndex: Int, private val layoutCalculator: LayoutCalculator, private val colour: Colour) : GraphicPosition {

    //True if we should use the left column (depends on the colour)
    override val x: ObservableNumberValue = if (colour == Colour.BLACK && positionIndex < LIMIT || colour == Colour.WHITE && positionIndex >= LIMIT) {
            layoutCalculator.getGraveyardXPosition(colour)
        } else {
            layoutCalculator.componentSize.add(layoutCalculator.getGraveyardXPosition(colour))
        }

    override val y: NumberBinding = layoutCalculator.componentSize
                .multiply(if (positionIndex < LIMIT) positionIndex else positionIndex - LIMIT)
}
