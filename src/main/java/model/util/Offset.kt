package model.util

import java.io.Serializable

/**
 * Un relative shift in position
 */
class Offset(verticalOffset: Int, horizontalOffset: Int) : Serializable {

    private val offset = IntArray(2)

    internal val verticalShift: Int
        get() = offset[0]

    internal val horizontalShift: Int
        get() = offset[1]

    init {
        offset[0] = verticalOffset
        offset[1] = horizontalOffset
    }

    override fun toString(): String {
        return "shift-" + offset[0] + "-down-" + offset[1] + "-up"
    }

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
