package ui.game.components

import engine.pieces.Piece
import javafx.beans.value.ObservableNumberValue
import javafx.scene.CacheHint
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import ui.game.layout.GraphicPosition


/**
 * The ratio for the size of the pane to the font size
 */
private const val SIZE_TO_FONT_RATIO = 0.75F

/**
 * A pane that displays a pieceMap
 *
 * @property piece    the pieceMap to show in the pane
 * @param position the position of the pieceMap
 * @param size     the size of the pane (width/height)
 */
class PiecePane(val piece: Piece, position: GraphicPosition, size: ObservableNumberValue) : StackPane() {

    /**
     * The text showing the pieceMap
     */
    private val text = Text()

    /**
     * The pane's current position
     */
    var currentPosition: GraphicPosition? = position
        private set

    init {
        //Bind the pane's width/height
        this.prefHeightProperty().bind(size)
        this.prefWidthProperty().bind(size)

        //Bind the pane to it's position
        bind(position)

        //Add the text to the pane
        setText()
        this.children.add(text)

        //Bind the text's font to the pane's size
        this.prefWidthProperty().addListener { _, _, newValue -> text.font = Font(newValue.toDouble() * SIZE_TO_FONT_RATIO) }
    }

    /**
     * Update the text based on the pieceMap (actually matters for pawn promotion where pawn becomes queen)
     */
    fun setText() {
        text.text = Character.toString(piece.unicode.toChar()) //Set the text to the unicode value of the pieceMap
    }

    /**
     * Binds the pieceMap to its position
     */
    @Synchronized
    fun bind(position: GraphicPosition) {
        this.currentPosition = position

        this.layoutXProperty().bind(position.x)
        this.layoutYProperty().bind(position.y)

        this.cacheHint = CacheHint.DEFAULT
    }

    /**
     * Unbinds the pieceMap from it's position
     */
    @Synchronized
    fun unBind() {
        this.currentPosition = null
        this.cacheHint = CacheHint.SPEED
        this.layoutXProperty().unbind()
        this.layoutYProperty().unbind()
    }

    /**
     * @return true if the pieceMap is at the position
     */
    @Synchronized
    fun isAtPosition(position: GraphicPosition): Boolean {
        return position == currentPosition
    }
}

