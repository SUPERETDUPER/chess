package ui.game.layout


import javafx.beans.value.ObservableValue

/**
 * Represents the X and Y coordinates (position) of a piece
 */
interface GraphicPosition {

    /**
     * @return the x coordinate for this position
     */
    val x: ObservableValue<Number>

    /**
     * @return the y coordinate for this position
     */
    val y: ObservableValue<Number>
}
