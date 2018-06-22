package ui.game.components

import javafx.beans.value.ObservableNumberValue
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import org.jetbrains.annotations.Contract
import ui.game.layout.SquareGraphicPosition

import java.util.function.Consumer

/**
 * Controls a square on the UI. The square can be highlighted blue or red.
 */
class SquarePane
/**
 * @param isWhite       true if the square is a white square
 * @param clickListener the method to call when the square is clicked
 * @param position      the squares position
 * @param size          the size of the square (width/height)
 */
(
        /**
         * True if the square is a white square (false if black/gray)
         */
        private val isWhite: Boolean, clickListener: Consumer<SquareGraphicPosition>, position: SquareGraphicPosition, size: ObservableNumberValue) : Rectangle() {
    /**
     * The different highlight modes
     */
    enum class Style {
        NORMAL,
        RED,
        BLUE
    }

    init {

        this.widthProperty().bind(size)
        this.heightProperty().bind(size)
        this.xProperty().bind(position.x)
        this.yProperty().bind(position.y)

        this.setOnMouseClicked { _ -> clickListener.accept(position) }

        setStyle(Style.NORMAL)  //Set the background colour
    }

    /**
     * @param style the new highlight style
     */
    fun setStyle(style: Style) {
        this.fill = getFillColour(style)
    }

    /**
     * @return the background fill colour for this highlight style
     */
    @Contract(pure = true)
    private fun getFillColour(style: Style): Paint {
        return when (style) {
            SquarePane.Style.BLUE -> if (isWhite) Color.LIGHTBLUE else Color.CORNFLOWERBLUE
            SquarePane.Style.RED -> Color.PALEVIOLETRED
            SquarePane.Style.NORMAL -> if (isWhite) Color.WHITE else Color.LIGHTGRAY
        }
    }
}
