package ui.game.components

import javafx.beans.value.ObservableNumberValue
import javafx.scene.CacheHint
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import engine.pieces.Piece
import ui.game.layout.GraphicPosition

/**
 * A pane that displays a piece
 */
class PiecePane
/**
 * @param piece    the piece to show
 * @param position the position of the piece
 * @param size     the size of the pane (width/height)
 */
(
        /**
         * The piece in the pane
         */
        val piece: Piece, position: GraphicPosition, size: ObservableNumberValue) : StackPane() {

    /**
     * The text showing the piece
     */
    private val text = Text()

    /**
     * The pane's current position
     */
    var currentPosition: GraphicPosition? = null
        private set

    init {
        this.currentPosition = position

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
     * Update the text based on the piece (actually matters for pawn promotion where pawn becomes queen)
     */
    fun setText() {
        text.text = Character.toString(piece.unicode.toChar()) //Set the text to the unicode value of the piece
    }

    /**
     * Binds the piece to its position
     */
    @Synchronized
    fun bind(position: GraphicPosition) {
        this.currentPosition = position

        this.layoutXProperty().bind(position.x)
        this.layoutYProperty().bind(position.y)

        this.cacheHint = CacheHint.DEFAULT
    }

    /**
     * Unbinds the piece from it's position
     */
    @Synchronized
    fun unBind() {
        this.currentPosition = null
        this.cacheHint = CacheHint.SPEED
        this.layoutXProperty().unbind()
        this.layoutYProperty().unbind()
    }

    /**
     * @return true if the piece is at the position
     */
    @Synchronized
    fun isAtPosition(position: GraphicPosition): Boolean {
        return position == currentPosition
    }

    companion object {
        /**
         * The ratio for the size of the pane to the font size
         */
        private const val SIZE_TO_FONT_RATIO = 0.75f
    }
}