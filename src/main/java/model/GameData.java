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
 * Représente le boardregion de gamewindow et quelle pièces sont les rois
 */
public class GameData implements Serializable {
    @NotNull
    private final BoardMap boardMap;

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
    private final EnumMap<Colour, King> rois = new EnumMap<>(Colour.class);

    /**
     * @param boardMap le boardregion de gamewindow
     */
    public GameData(@NotNull BoardMap boardMap) {
        for (Piece piece : boardMap.iteratePieces()) {
            if (piece instanceof King) {
                if (rois.containsKey(piece.getColour()))
                    throw new RuntimeException("Il y a deux rois de la même couleur");

                rois.put(piece.getColour(), (King) piece);
            }
        }

        this.boardMap = boardMap;
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
    public BoardMap getBoardMap() {
        return boardMap;
    }

    /**
     * @param colour la colour du roi demandé
     */
    @NotNull
    King getRoi(Colour colour) {
        return rois.get(colour);
    }

    /**
     * @return la liste mouvements possible et légal pour cette colour
     */
    @NotNull
    public Collection<Move> getAllLegalMoves(Colour colour) {
        return filterOnlyLegal(boardMap.getAllMoves(colour), colour);
    }

    /**
     * @param moves une liste de moves légals et illégals
     * @param verifierPour la couleur du player qu'il faut vérifier
     * @return la liste de moves avec que les moves légals
     */
    @NotNull
    public Collection<Move> filterOnlyLegal(Collection<Move> moves, Colour verifierPour) {
        Collection<Move> legalMoves = new ArrayList<>();

        //Pour chaque moves appliquer et vérifier si l'on attaque le roi
        for (Move move : moves) {
            move.appliquer(this);

            if (!getBoardMap().isPieceAttaquer(rois.get(verifierPour))) {
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