package ui.game;

import javafx.beans.binding.DoubleBinding;
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
import ui.game.layout.GraveyardController;
import ui.game.layout.SquareGraphicPosition;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
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
     * A map of the two graveyards and their colours
     */
    @NotNull
    private final EnumMap<Colour, GraveyardController> graveyardControllers = new EnumMap<>(Colour.class);

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

        //Create graveyards
        GraveyardController leftGraveyard = new GraveyardController(
                this.heightProperty(),
                false
        );

        this.graveyardControllers.put(Colour.WHITE, leftGraveyard);

        this.graveyardControllers.put(
                Colour.BLACK,
                new GraveyardController(
                        this.heightProperty(),
                        true,
                        this.heightProperty().add(leftGraveyard.getTotalWidth())
                )
        );

        DoubleBinding componentSize = heightProperty().divide(Position.LIMIT);

        //For each position create square
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            SquareGraphicPosition graphicPosition = new SquareGraphicPosition(position, this.heightProperty(), leftGraveyard.getTotalWidth());

            //Create the square
            SquarePane squarePane = new SquarePane(
                    (position.getColumn() + position.getRow()) % 2 == 0, //Calculates wheather the sqaure should be white or black (gray) (top-left is white)
                    this::handleClick,
                    graphicPosition,
                    componentSize
            );

            //Add the square to the list
            boardSquares.add(position, squarePane);

            //If there is a piece at this position create a piecePane it
            Piece piece = gameData.getBoard().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, graphicPosition, componentSize);

                piecePane.setOnMousePressed(event -> handleClick(piecePane.getCurrentPosition()));

                //Add the piecePane to the map
                piecePanes.put(piece, piecePane);
            }
        }

        //For each already eaten piece create a piecePane and add to graveyard
        for (Piece piece : gameData.getEatenPieces()) {
            PiecePane piecePane = new PiecePane(piece, graveyardControllers.get(piece.getColour()).getNextGraveyardPosition(), componentSize);
            piecePane.setOnMouseClicked(event -> this.handleClick(piecePane.getCurrentPosition()));

            piecePanes.put(piece, piecePane);
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
     * @param colour the colour of the player that should submit the move
     */
    void requestMove(Consumer<Move> callback, Colour colour) {
        this.moveRequest = new MoveRequest(callback, colour);
    }

    /**
     * Called when a component (piece or square) is clicked
     *
     * @param panePosition the components position
     */
    //TODO separate piece click for readability
    private void handleClick(GraphicPosition panePosition) {
        //If not a square's position do nothing
        if (!(panePosition instanceof SquareGraphicPosition)) return;

        //If there are no active moveRequests do nothing
        if (moveRequest == null || moveRequest.isSubmited()) return;

        //Get the position and piece (might be null - empty square)
        Position position = ((SquareGraphicPosition) panePosition).getPosition();
        @Nullable Piece pieceClicked = gameData.getBoard().getPiece(position);

        //If no piece already selected
        if (!highlightController.isSelected()) {
            //If empty square or piece is for other player do nothing
            if (pieceClicked == null || moveRequest.getColour() != pieceClicked.getColour()) return;

            //Calculate possible moves for piece
            Collection<Move> moves = gameData.filterOnlyLegal(pieceClicked.generatePossibleMoves(gameData.getBoard(), position), pieceClicked.getColour());

            //Highlight those moves
            highlightController.select(position, moves);
        } else {
            //If not a move option remove clear highlight and quit
            if (!highlightController.isPossibleMove(position)) {
                highlightController.eraseSelection();
                return;
            }

            //Get selected move and apply (and erase highlight)
            Move selectedMove = highlightController.getPossibleMove(position);
            highlightController.eraseSelection();
            moveRequest.submit(selectedMove);
        }
    }

    /**
     * Called when the model changes to update the display
     */
    private synchronized void updateBoard() {
//        while (animationController.isRunning().get()) Thread.yield();

        //For each piece on the board
        for (Piece piece : gameData.getBoard().iteratePieces()) {
            Position position = gameData.getBoard().getPosition(piece);

            //Create graphic position
            SquareGraphicPosition panePosition = new SquareGraphicPosition(position, this.heightProperty(), graveyardControllers.get(Colour.WHITE).getTotalWidth());

            //Get piecePane
            PiecePane piecePane = piecePanes.get(piece);

            //If piece pane not already at position make animation
            if (!piecePane.isAtPosition(panePosition)) animationController.addAnimation(piecePane, panePosition);
        }

        //For each piece in graveyard
        for (Piece piece : gameData.getEatenPieces()) {
            PiecePane piecePane = piecePanes.get(piece);

            //If graveyard does not already contain piece make animation to move piece to graveyard
            if (!graveyardControllers.get(piece.getColour()).isInGraveyard(piece))
                animationController.addAnimation(
                        piecePane,
                        graveyardControllers.get(piece.getColour()).getNextGraveyardPosition()
                );
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
        double ratio = 1;

        for (GraveyardController graveyardController : graveyardControllers.values()) {
            ratio += graveyardController.getTotalWidthRatio();
        }

        return height * ratio;
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
        private boolean isSubmited = false;

        MoveRequest(@NotNull Consumer<Move> callback, @NotNull Colour colour) {
            this.moveCallback = callback;
            this.colour = colour;
        }

        void submit(Move move) {
            isSubmited = true;
            moveCallback.accept(move);
        }

        boolean isSubmited() {
            return isSubmited;
        }

        @NotNull
        Colour getColour() {
            return colour;
        }
    }
}