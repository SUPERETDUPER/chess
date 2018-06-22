package engine.moves

import engine.GameData
import engine.pieces.Piece
import engine.util.Position

/**
 * A move that moves one piece to a new square (and eats the piece at that square if it exists)
 */
internal open class BaseMove(start: Position, destination: Position) : Move(start, destination) {
    private var eatenPiece: Piece? = null

    /**
     * @return 0 if no piece was eating (moving a piece does not change the board value). If a piece was eaten the value is - the piece's value
     */
    override val value: Int
        get() = if (eatenPiece == null) 0 else -eatenPiece!!.signedValue

    override fun applyToGame(data: GameData) {
        piece = data.board.removePiece(start) //Remove the piece from the starting position
        eatenPiece = data.board.add(end, piece!!) //Place the piece at the ending position and get the piece that was replaced

        if (eatenPiece != null) {
            data.getEatenPieces(eatenPiece!!.colour).push(eatenPiece) //Add the eaten to the stack in the game data
        }
    }

    override fun undoToGame(data: GameData) {
        data.board.movePiece(start, piece!!) //Move the piece to its starting position

        //If a piece was eaten remove it from the stack and add it to the board
        if (eatenPiece != null) {
            data.getEatenPieces(eatenPiece!!.colour).pop()
            data.board.add(end, eatenPiece!!)
        }
    }
}
