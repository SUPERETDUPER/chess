package model;

import model.moves.Move;
import model.pieces.King;
import model.pieces.Piece;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * Represents the board state (piece's positions) and the pieces that were eaten
 */
public class GameData implements Serializable {
    @NotNull
    private final BoardMap board;

    @NotNull
    private final EnumMap<Colour, Stack<Piece>> eatenPieces = new EnumMap<>(Colour.class);

    /**
     * A list of completed moves
     */
    private final LinkedList<Move> pastMoves = new LinkedList<>();

    /**
     * The kings for each player.
     */
    @NotNull
    private final EnumMap<Colour, King> kings = new EnumMap<>(Colour.class);

    public GameData(@NotNull BoardMap boardMap) {
        this.board = boardMap;

        for (Colour colour : Colour.values()) {
            eatenPieces.put(colour, new Stack<>());
        }

        for (Piece piece : boardMap.iteratePieces()) {
            if (piece instanceof King) {
                if (kings.containsKey(piece.getColour()))
                    throw new RuntimeException("There are two kings for the same player");

                kings.put(piece.getColour(), (King) piece);
            }
        }
    }

    @NotNull
    public BoardMap getBoard() {
        return board;
    }

    /**
     * @return the king of this colour
     */
    @NotNull
    King getKing(Colour colour) {
        return kings.get(colour);
    }

    /**
     * @return a collection of possible legal moves that can be player
     */
    @NotNull
    public Collection<Move> getPossibleLegalMoves(Colour colour) {
        return filterOnlyLegal(getAllPossibleMoves(colour), colour);
    }

    /**
     * @param moves        a collection of legal and illegal moves
     * @param verifierPour legal for who (will remove all moves that throw this colour's king in check)
     * @return a filter collection containing only legal moves
     */
    @NotNull
    public Collection<Move> filterOnlyLegal(@NotNull Collection<Move> moves, Colour verifierPour) {
        Collection<Move> legalMoves = new ArrayList<>();

        //For each move apply it and check if the king is in check
        for (Move move : moves) {
            move.apply(this);

            if (!isPieceAttacked(kings.get(verifierPour))) {
                legalMoves.add(move);
            }

            move.undo(this);
        }

        return legalMoves;
    }

    /**
     * @return true if the piece is being attacked by another piece
     */
    public boolean isPieceAttacked(@NotNull Piece piece) {
        return isPositionAttacked(board.getPosition(piece), piece.getColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE);
    }

    public boolean isPositionAttacked(Position position, Colour byWho) {
        for (Piece attacker : board.iteratePieces()) {
            //If piece opposite color check if attacks position
            if (attacker.getColour() == byWho && attacker.isAttackingPosition(this, position)) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    private Set<Move> getAllPossibleMoves(Colour colour) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : board.iteratePieces()) {
            if (piece.getColour() == colour) {
                moves.addAll(piece.generatePossibleMoves(this, board.getPosition(piece)));
            }
        }
        return moves;
    }

    /**
     * @return the stack of eaten pieces for this colour
     */
    public Stack<Piece> getEatenPieces(Colour colour) {
        return eatenPieces.get(colour);
    }

    @NotNull
    public LinkedList<Move> getPastMoves() {
        return pastMoves;
    }
}