package engine.moves

import engine.GameData
import engine.pieces.Pawn
import engine.pieces.Piece
import engine.util.Position

internal class EnPassantMove(start: Position, destination: Position) : BaseMove(start, destination) {
    private var eatenPiece: Piece? = null

    override val value: Int
        get() = -eatenPiece!!.signedValue

    override fun applyToGame(data: GameData) {
        super.applyToGame(data)

        eatenPiece = data.board.removePiece(end.shift((piece as Pawn).backward))
        data.getEatenPieces(eatenPiece!!.colour).push(eatenPiece)
    }

    override fun undoToGame(data: GameData) {
        data.getEatenPieces(eatenPiece!!.colour).pop()
        data.board.add(end.shift((piece as Pawn).backward), eatenPiece!!)

        super.undoToGame(data)
    }
}
