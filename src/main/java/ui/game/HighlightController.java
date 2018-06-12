package ui.game;

import model.moves.Move;
import model.util.Board;
import model.util.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.game.components.SquarePane;

import java.util.Collection;
import java.util.HashMap;

/**
 * Controls how the squares should be highlighted
 * When a piece is highlighted, the piece's square is made red and the possible moves more the piece can go is made blue
 */
class HighlightController {
    /**
     * The list of squares at each position
     */
    @NotNull
    private final Board<SquarePane> squares;

    /**
     * The list of possible moves for the highlighted piece
     * Empty if no piece is highlighted
     */
    @NotNull
    private final HashMap<Position, Move> possibleMoves = new HashMap<>();

    /**
     * The selected piece's position
     */
    @Nullable
    private Position selectedPosition;

    /**
     * @param squares the list of squares
     */
    HighlightController(@NotNull Board<SquarePane> squares) {
        this.squares = squares;
    }

    /**
     * Selects a square (with a piece on it) and highlights the possible moves for that piece to move to
     *
     * @param position      the selected position
     * @param possibleMoves a list of possible moves for that position
     */
    void select(@NotNull Position position, Collection<Move> possibleMoves) {
        this.eraseSelection();

        //Highlight position
        this.selectedPosition = position;
        squares.get(position).setStyle(SquarePane.Style.RED);

        //For each move
        for (Move move : possibleMoves) {
            Position end = move.getEnd();

            squares.get(end).setStyle(SquarePane.Style.BLUE); //Highlight end position
            this.possibleMoves.put(end, move); //Add to list
        }
    }

    /**
     * Erases the current selection (removes highlight)
     */
    void eraseSelection() {
        //If nothing selected return
        if (selectedPosition == null) return;

        //Remove highlight from selected position
        squares.get(selectedPosition).setStyle(SquarePane.Style.NORMAL);

        //Remove highlight from each option
        for (Position position : possibleMoves.keySet()) {
            squares.get(position).setStyle(SquarePane.Style.NORMAL);
        }

        //Clear list and selectedPosition
        possibleMoves.clear();
        selectedPosition = null;
    }

    boolean isSelected() {
        return selectedPosition != null;
    }

    /**
     * @return true if the position is one of the possible moves (highlighted in blue)
     */
    boolean isPossibleMove(Position position) {
        return possibleMoves.containsKey(position);
    }

    /**
     * @return the move associated with the position
     */
    Move getPossibleMove(Position position) {
        return possibleMoves.get(position);
    }
}
