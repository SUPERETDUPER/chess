package ui.game.layout

import javafx.beans.binding.NumberBinding
import javafx.beans.value.ObservableNumberValue
import engine.util.Colour
import engine.util.Position

/**
 * A position in the graveyard
 */
class GraveyardGraphicPosition internal constructor(private val layoutCalculator: LayoutCalculator,
                                                    /**
                                                     * The position in the stack of the piece (one more than the past)
                                                     */
                                                    private val positionIndex: Int, private val colour: Colour) : GraphicPosition {

    override//True if we should use the left column (depends on positionIndex and isLeftToRight)
    val x: ObservableNumberValue
        get() = if (colour == Colour.BLACK && positionIndex < Position.LIMIT || colour == Colour.WHITE && positionIndex >= Position.LIMIT) {
            layoutCalculator.getGraveyardXOffset(colour)
        } else {
            layoutCalculator.componentSize.add(layoutCalculator.getGraveyardXOffset(colour))
        }

    override val y: NumberBinding
        get() = layoutCalculator.componentSize
                .multiply(if (positionIndex < Position.LIMIT) positionIndex else positionIndex - Position.LIMIT)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return if (other !is GraveyardGraphicPosition) false else other.positionIndex == this.positionIndex
    }

    override fun toString(): String {
        return "Graveyard positionIndex: $positionIndex"
    }
}
