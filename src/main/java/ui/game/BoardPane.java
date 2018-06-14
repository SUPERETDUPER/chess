package ui.game;

import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import model.Game;
import model.GameData;
import model.moves.Move;
import model.pieces.Piece;
import model.util.Board;
import model.util.Colour;
import model.util.Position;
import model.util.PositionIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.game.components.PiecePane;
import ui.game.components.SquarePane;
import ui.game.layout.GraphicPosition;
import ui.game.layout.GraveyardGraphicPosition;
import ui.game.layout.LayoutCalculator;
import ui.game.layout.SquareGraphicPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Controls the main region containing the board and graveyards
 */
public class BoardPane extends Pane {
    /**
     * The list of squares
     */
    //Warning if refactoring board data structure make sure highlight controller get populated list
    @NotNull
    private final Board<SquarePane> boardSquares = new Board<>();

    private final LayoutCalculator layoutCalculator = new LayoutCalculator(heightProperty());

    @NotNull
    private final AnimationController animationController = new AnimationController();

    @NotNull
    private final HighlightController highlightController = new HighlightController(boardSquares);

    /**
     * A map of pieces and their associated piecePane
     */
    @NotNull
    private final Map<Piece, PiecePane> piecePanes = new HashMap<>();

    /**
     * The data passed through the constructor
     */
    @NotNull
    private final GameData gameData;

    /**
     * Object representing the current move request
     */
    @Nullable
    private MoveRequest moveRequest;

    /**
     * @param game the game model
     */
    BoardPane(@NotNull Game game) {
        this.gameData = game.getGameData();

        //For each position create square
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            SquareGraphicPosition graphicPosition = layoutCalculator.createSquarePosition(position);

            //Create the square
            SquarePane squarePane = new SquarePane(
                    (position.getColumn() + position.getRow()) % 2 == 0, //Calculates wheather the sqaure should be white or black (gray) (top-left is white)
                    this::squareClick,
                    graphicPosition,
                    layoutCalculator.getComponentSize()
            );

            //Add the square to the list
            boardSquares.add(position, squarePane);

            //If there is a piece at this position create a piecePane it
            Piece piece = gameData.getBoard().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, graphicPosition, layoutCalculator.getComponentSize());

                piecePane.setOnMousePressed(event -> pieceClick(piecePane));

                //Add the piecePane to the map
                piecePanes.put(piece, piecePane);
            }
        }

        //For each already eaten piece create a piecePane and add to graveyard of that colour
        for (Colour colour : Colour.values()) {
            int graveyardPositionOfNext = 0;

            for (Piece eatenPiece : gameData.getEatenPieces(colour)) {
                PiecePane piecePane = new PiecePane(
                        eatenPiece,
                        layoutCalculator.createGraveyardPosition(colour, graveyardPositionOfNext++),
                        layoutCalculator.getComponentSize()
                );

                piecePane.setOnMouseClicked(event -> this.pieceClick(piecePane));

                piecePanes.put(eatenPiece, piecePane);
            }
        }

        //Add all the squares and pieces to the board
        this.getChildren().addAll(boardSquares.getData());
        this.getChildren().addAll(piecePanes.values());

        game.addBoardChangeListener(this::updateBoard); //If the board model changes update the display

        //When the game is no longer waiting for a move, wait for all animations to finish then trigger the next player to play
        game.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Game.Status.INACTIVE) {
                if (animationController.isRunning())
                    //When animations finish remove listener and trigger next player
                    animationController.setOnFinishListener(() -> {
                        animationController.setOnFinishListener(null);
                        game.notifyNextPlayer();
                    });

                else game.notifyNextPlayer(); //If animations not running trigger next player immediately
            }
        });
    }

    /**
     * Called by a player to request the board to record a users move
     *
     * @param callback the callback method to which the move should be submitted
     * @param colour   the colour of the player that should submit the move
     */
    void requestMove(Consumer<Move> callback, Colour colour) {
        this.moveRequest = new MoveRequest(callback, colour);
    }

    private void pieceClick(PiecePane piecePane) {
        //If piece not at a square do nothing (if it's in graveyard)
        if (!(piecePane.getCurrentPosition() instanceof SquareGraphicPosition)) return;

        //If there are no active moveRequests do nothing
        if (moveRequest == null || moveRequest.isSubmitted()) return;

        Piece pieceClicked = piecePane.getPiece();
        Position position = ((SquareGraphicPosition) piecePane.getCurrentPosition()).getPosition();

        //If piece already selected this click was to submit move
        if (highlightController.isSelected()) {
            trySubmittingMove(position);
            return;
        }

        //If move request for other player do nothing
        if (moveRequest.getColour() != pieceClicked.getColour()) return;


        //Calculate possible moves and highlight those moves
        highlightController.select(position,
                gameData.filterOnlyLegal(
                        pieceClicked.generatePossibleMoves(gameData, position),
                        pieceClicked.getColour()
                )
        );
    }

    /**
     * Called when a component (piece or square) is clicked
     *
     * @param panePosition the components position
     */
    private void squareClick(SquareGraphicPosition panePosition) {
        //If no piece already selected do nothing
        if (!highlightController.isSelected()) return;

        //Get the position and try submitting move
        trySubmittingMove(panePosition.getPosition());
    }

    private void trySubmittingMove(Position position) {
        //If not a move option remove highlight
        if (!highlightController.isPossibleMove(position)) {
            highlightController.eraseSelection();
            return;
        }

        //Else get selected move and apply (and erase highlight)
        Move selectedMove = highlightController.getPossibleMove(position);
        highlightController.eraseSelection();
        moveRequest.submit(selectedMove);
    }

    /**
     * Called when the model changes to update the display
     */
    private synchronized void updateBoard() {
        //For each piece on the board
        for (Piece piece : gameData.getBoard().iteratePieces()) {
            Position position = gameData.getBoard().getPosition(piece);

            //Create graphic position
            GraphicPosition panePosition = layoutCalculator.createSquarePosition(position);

            //Get piecePane
            PiecePane piecePane = piecePanes.get(piece);

            //If piece pane not already at position make animation
            if (!piecePane.isAtPosition(panePosition)) animationController.addAnimation(piecePane, panePosition);
        }

        //For each colour of pieces
        for (Colour colour : Colour.values()) {
            //For each piece in the graveyard
            Stack<Piece> eatenPieces = gameData.getEatenPieces(colour);
            for (Piece piece : eatenPieces) {
                PiecePane piecePane = piecePanes.get(piece);

                //If piecePane not already in graveyard move the piecePane to the graveyard
                if (!(piecePane.getCurrentPosition() instanceof GraveyardGraphicPosition))
                    animationController.addAnimation(
                            piecePane,
                            layoutCalculator.createGraveyardPosition(colour, eatenPieces.size() - 1)
                    );
            }
        }
    }

    //So that the width resizes based on the height
    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Sets the preferred width as a factor of the height
     */
    @Override
    protected double computePrefWidth(double height) {
        return height * layoutCalculator.getWidthRatio(); //The board takes up 1 * height and the graveyards (4 / 8) * height
    }

    /**
     * An object representing a request to the UI for a move
     */
    private static class MoveRequest {
        /**
         * The callback method to which the selected move should be submitted
         */
        @NotNull
        private final Consumer<Move> moveCallback;

        /**
         * The colour of the player that should submit the move
         */
        @NotNull
        private final Colour colour;

        /**
         * True if the move has already been submited
         */
        private boolean isSubmitted = false;

        MoveRequest(@NotNull Consumer<Move> callback, @NotNull Colour colour) {
            this.moveCallback = callback;
            this.colour = colour;
        }

        void submit(Move move) {
            isSubmitted = true;
            moveCallback.accept(move);
        }

        boolean isSubmitted() {
            return isSubmitted;
        }

        @NotNull
        Colour getColour() {
            return colour;
        }
    }
}