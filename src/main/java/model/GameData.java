package model;

import model.moves.Move;
import model.pieces.King;
import model.pieces.Piece;
import model.util.BoardMap;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Stack;

/**
 * Représente le boardregion de game et quelle pièces sont les kings
 */
public class GameData implements Serializable {
    @NotNull
    private final BoardMap board;

    @NotNull
    private final Stack<Piece> eatenPieces = new Stack<>();

    /**
     * Le listener pour quand le boardregion change
     */
    @Nullable
    transient private Runnable changeListener;

    /**
     * La liste de roi associé à chaque couleur
     */
    @NotNull
    private final EnumMap<Colour, King> kings = new EnumMap<>(Colour.class);

    /**
     * @param board le boardregion de game
     */
    public GameData(@NotNull BoardMap board) {
        for (Piece piece : board.iteratePieces()) {
            if (piece instanceof King) {
                if (kings.containsKey(piece.getColour()))
                    throw new RuntimeException("Il y a deux kings de la même couleur");

                kings.put(piece.getColour(), (King) piece);
            }
        }

        this.board = board;
    }

    public void setChangeListener(@NotNull Runnable changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * Appelé par {@link Game} pour notifier qu'un moves sur le boardregion a été effectué
     */
    void notifyListenerOfChange() {
        changeListener.run();
    }

    @NotNull
    public BoardMap getBoard() {
        return board;
    }

    /**
     * @param colour la colour du roi demandé
     */
    @NotNull
    King getKing(Colour colour) {
        return kings.get(colour);
    }

    /**
     * @return la liste mouvements possible et légal pour cette colour
     */
    @NotNull
    public Collection<Move> getAllLegalMoves(Colour colour) {
        return filterOnlyLegal(board.getAllPossibleMoves(colour), colour);
    }

    /**
     * @param moves une liste de moves légals et illégals
     * @param verifierPour la couleur du player qu'il faut vérifier
     * @return la liste de moves avec que les moves légals
     */
    @NotNull
    public Collection<Move> filterOnlyLegal(Collection<Move> moves, Colour verifierPour) {
        Collection<Move> legalMoves = new ArrayList<>();

        //Pour chaque moves apply et vérifier si l'on attaque le roi
        for (Move move : moves) {
            move.apply(this);

            if (!getBoard().isPieceAttacked(kings.get(verifierPour))) {
                legalMoves.add(move);
            }

            move.undo(this);
        }

        return legalMoves;
    }

    @NotNull
    public Stack<Piece> getEatenPieces() {
        return eatenPieces;
    }
}