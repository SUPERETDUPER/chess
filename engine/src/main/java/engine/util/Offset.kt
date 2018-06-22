package engine.util

import java.io.Serializable

/**
 * Un relative shift in position
 */
data class Offset(val verticalOffset: Int, val horizontalOffset: Int) : Serializable {

    companion object {
        val TOP_LEFT = Offset(-1, -1)
        val UP = Offset(-1, 0)
        val TOP_RIGHT = Offset(-1, 1)
        val LEFT = Offset(0, -1)
        val RIGHT = Offset(0, 1)
        val BOTTOM_LEFT = Offset(1, -1)
        val DOWN = Offset(1, 0)
        val BOTTOM_RIGHT = Offset(1, 1)
    }
}
