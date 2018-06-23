package ui.game.layout

import javafx.beans.binding.NumberBinding
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableNumberValue
import engine.util.Colour
import engine.util.Position

/**
 * Calculates the position of the elements on the display
 */
class LayoutCalculator(boardHeight: ReadOnlyDoubleProperty) {
    val componentSize: NumberBinding = boardHeight.divide(Position.LIMIT)

    internal val boardXOffset: ObservableNumberValue
        get() = componentSize.multiply(2)

    val widthRatio: Double = 1 + 4.0 / Position.LIMIT

    internal fun getGraveyardXOffset(colour: Colour): ObservableNumberValue {
        return if (colour == Colour.WHITE)
            SimpleIntegerProperty(0)
        else
            componentSize.multiply(Position.LIMIT + 2)
    }

    fun createGraveyardPosition(colour: Colour, position: Int): GraphicPosition {
        return GraveyardGraphicPosition(position, this, colour)
    }

    fun createSquarePosition(position: Position): SquareGraphicPosition {
        return SquareGraphicPosition(position, this)
    }
}
