package engine.moves

import engine.GameData
import engine.pieces.Piece
import engine.util.Position

/**
 * A move that moves one pieceMap to a new square (and eats the pieceMap at that square if it exists)
 */
internal open class BaseMove(start: Position, destination: Position) : Move(start, destination) {
    protected var eatenPiece: Piece? = null

    /**
     * @return 0 if no pieceMap was eating (moving a pieceMap does not change the pieceMap value). If a pieceMap was eaten the value is - the pieceMap's value
     */
    override val value: Int
        get() = -(eatenPiece?.signedValue ?: 0)


    override fun applyToGame(data: GameData) {
        piece = data.pieceMap.removePiece(start) //Remove the pieceMap from the starting position
        eatenPiece = data.pieceMap.put(end, piece) //Place the pieceMap at the ending position and get the pieceMap that was replaced

        eatenPiece?.let {
            data.getEatenPieces(it.colour).push(it) //Add the eaten to the stack in the game data
        }
    }

    override fun undoToGame(data: GameData) {
        data.pieceMap.movePiece(start, piece) //Move the pieceMap to its starting position

        //If a pieceMap was eaten remove it from the stack and put it to the pieceMap
        eatenPiece?.let {
            data.getEatenPieces(it.colour).pop()
            data.pieceMap.put(end, it)
        }
    }
}
