package ui.game.components

import javafx.beans.value.ObservableNumberValue
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import ui.game.layout.SquareGraphicPosition

/**
 * Controls a square on the UI. The square can be highlighted blue or red.
 *
 * @property isWhite       true if the square is a white square (false if black/gray)
 * @param clickListener the method to call when the square is clicked
 * @param position      the squares position
 * @param size          the size of the square (width/height)
 */
class SquarePane(private val isWhite: Boolean, clickListener: (SquareGraphicPosition) -> Unit, position: SquareGraphicPosition, size: ObservableNumberValue) : Rectangle() {
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

        this.setOnMouseClicked { clickListener(position) }

        setStyle(Style.NORMAL)  //Set the background colour
    }

    /**
     * @param style the new highlight style
     */
    fun setStyle(style: Style) {
        this.fill = when (style) {
            Style.BLUE -> if (isWhite) Color.LIGHTBLUE else Color.CORNFLOWERBLUE
            Style.RED -> Color.PALEVIOLETRED
            Style.NORMAL -> if (isWhite) Color.WHITE else Color.LIGHTGRAY
        }
    }

}
