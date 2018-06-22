package ui.game.layout

import javafx.beans.binding.NumberBinding
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableNumberValue
import model.util.Colour
import model.util.Position

/**
 * Calculates the position of the elements on the display
 */
class LayoutCalculator(boardHeight: ReadOnlyDoubleProperty) {
    val componentSize: NumberBinding

    internal val boardXOffset: ObservableNumberValue
        get() = componentSize.multiply(2)

    val widthRatio: Double
        get() = 1 + 4.0 / Position.LIMIT

    init {
        this.componentSize = boardHeight.divide(Position.LIMIT)
    }

    internal fun getGraveyardXOffset(colour: Colour): ObservableNumberValue {
        return if (colour == Colour.WHITE)
            SimpleIntegerProperty(0)
        else
            componentSize.multiply(Position.LIMIT + 2)
    }

    fun createGraveyardPosition(colour: Colour, position: Int): GraphicPosition {
        return GraveyardGraphicPosition(this, position, colour)
    }

    fun createSquarePosition(position: Position): SquareGraphicPosition {
        return SquareGraphicPosition(position, this)
    }
}
