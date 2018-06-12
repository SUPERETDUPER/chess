package gui.gamewindow.boardregion;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gui.gamewindow.boardregion.components.PiecePane;
import gui.gamewindow.boardregion.components.SquarePane;
import gui.gamewindow.boardregion.layout.GraveyardController;
import gui.gamewindow.boardregion.layout.Layout;
import gui.gamewindow.boardregion.layout.SquareLayout;
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

import java.util.Collection;
import java.util.EnumMap;

/**
 * Controle la zone du boardregion de gamewindow
 */
public class BoardPane extends Pane {

    /**
     * La liste de boardSquares
     */
    @NotNull
    private final Board<SquarePane> boardSquares = new Board<>();

    /**
     * La liste de pièces
     */
    @NotNull
    private final BiMap<Piece, PiecePane> piecePanes = HashBiMap.create(32);

    /**
     * La controlleur d'animation
     */
    @NotNull
    private final AnimationController animationController = new AnimationController();

    /**
     * Le util de gamewindow et les rois
     */
    @NotNull
    private final GameData gameData;

    @NotNull
    private final HighlightController highlightController = new HighlightController(boardSquares);

    @NotNull
    private final EnumMap<Colour, GraveyardController> graveyardControllers = new EnumMap<>(Colour.class);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private MoveRequest moveRequest;

    /**
     * @param game le model
     */
    public BoardPane(@NotNull Game game) {
        this.gameData = game.getGameData();

        //Créer les graveyards et les add à la liste
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

        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            SquareLayout layout = new SquareLayout(position, this.heightProperty(), leftGraveyard.getTotalWidth());

            //Créer la case
            SquarePane squarePane = new SquarePane(
                    (position.getColumn() + position.getRow()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    this::handleClick,
                    layout //La position de la case
            );

            //Ajouter la case à la liste
            boardSquares.add(position, squarePane);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = gameData.getBoard().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, layout);

                //Ajouter les listeners
                piecePane.setOnMousePressed(event -> handleClick(piecePane.getCurrentPosition()));

                //Ajouter la pièce à la liste de pièce
                piecePanes.put(piece, piecePane);
            }
        }

        for (Piece piece : gameData.getEatenPieces()) {
            PiecePane piecePane = new PiecePane(piece, graveyardControllers.get(piece.getColour()).getNextGraveyardPosition());
            piecePane.setOnMouseClicked(event -> this.handleClick(piecePane.getCurrentPosition()));

            piecePanes.put(piece, piecePane);
        }

        //Ajouter toutes pièces au util
        this.getChildren().addAll(boardSquares.getValues());
        this.getChildren().addAll(piecePanes.values());

        this.gameData.setChangeListener(this::updateBoard); // Si il y a un changement replacer les pièces

        game.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Game.Status.INACTIVE) {
                if (animationController.isRunning())
                    //TODO remove listener when done
                    animationController.setOnFinishListener(game::notifyNextPlayer);
                else game.notifyNextPlayer();
            }
        });
    }

    /**
     * Appelé par un player pour demander au util d'enregistrer le moves du player humain
     *
     * @param moveRequest l'information sur le moves demandé
     */
    void requestMove(MoveRequest moveRequest) {
        this.moveRequest = moveRequest;
    }

    /**
     * Quand une position est appuyé
     *
     * @param panePosition la position est appuyé
     */
    private void handleClick(Layout panePosition) {
        if (!(panePosition instanceof SquareLayout)) return;

        Position position = ((SquareLayout) panePosition).getPosition();

        //Si aucune moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        //Obtenir la pièce à cette position
        Piece pieceClicked = gameData.getBoard().getPiece(position);

        //Si rien n'est sélectionné
        if (!highlightController.isSelected()) {
            //Si la pièce existe et elle est de la couleur du moveRequest
            if (pieceClicked != null && moveRequest.getColour() == pieceClicked.getColour()) {

                //Calculer les mouvements possibles
                Collection<Move> moves = gameData.filterOnlyLegal(pieceClicked.generateAllMoves(gameData.getBoard(), position), pieceClicked.getColour());

                highlightController.select(position, moves); //Sélectionner
            }
        } else {
            //Si la case est une des options apply le movement
            if (highlightController.isPossibleMove(position)) {
                Move moveChoisi = highlightController.getPossibleMove(position);
                highlightController.eraseSelection();
                moveRequest.apply(moveChoisi);
            } else {
                highlightController.eraseSelection(); //Déselectionner tout
            }
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private synchronized void updateBoard() {
//        while (animationController.isRunning().get()) Thread.yield();

        for (Piece piece : gameData.getBoard().iteratePieces()) {
            Position position = gameData.getBoard().getPosition(piece);

            SquareLayout panePosition = new SquareLayout(position, this.heightProperty(), graveyardControllers.get(Colour.WHITE).getTotalWidth());
            PiecePane piecePane = piecePanes.get(piece);

            if (!piecePane.isAtPosition(panePosition)) {
                animationController.addAnimation(piecePane,
                        panePosition
                );
            }
        }

        for (Piece piece : gameData.getEatenPieces()) {
            PiecePane piecePane = piecePanes.get(piece);
            if (!graveyardControllers.get(piece.getColour()).isInGraveyard(piece)) {
                animationController.addAnimation(
                        piecePane,
                        graveyardControllers.get(piece.getColour()).getNextGraveyardPosition()
                );
            }
        }
    }

    @Override
    public Orientation getContentBias() {
        return Orientation.VERTICAL;
    }

    /**
     * Définit la largeur préféré comme étant dépendant de la hauteur
     */
    @Override
    protected double computePrefWidth(double height) {
        double ratio = 1;

        for (GraveyardController graveyardController : graveyardControllers.values()) {
            ratio += graveyardController.getTotalWidthRatio();
        }

        return height * ratio;
    }
}