package engine.moves

import engine.GameData
import engine.pieces.Pawn
import engine.util.Position

internal class EnPassantMove(start: Position, destination: Position) : BaseMove(start, destination) {
    //Do not use eaten from base move because will add it to destination on undo
    private lateinit var enPassantEatenPiece: Pawn

    override val value: Int
            get() = -enPassantEatenPiece.signedValue

    override fun applyToGame(data: GameData) {
        super.applyToGame(data)

        enPassantEatenPiece = data.pieceMap.removePiece(end + (piece as Pawn).backwardShift) as Pawn

        data.getEatenPieces(enPassantEatenPiece.colour).push(enPassantEatenPiece)
    }

    override fun undoToGame(data: GameData) {
        data.getEatenPieces(enPassantEatenPiece.colour).pop()
        data.pieceMap.put(end + (piece as Pawn).backwardShift, enPassantEatenPiece)

        super.undoToGame(data)
    }
}