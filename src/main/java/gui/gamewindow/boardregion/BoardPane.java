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
     * La liste de cases
     */
    @NotNull
    private final Board<SquarePane> cases = new Board<>();

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
    private final HighlightController highlightController = new HighlightController(cases);

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

        //Créer les graveyards et les ajouter à la liste
        GraveyardController graveyardBlanc = new GraveyardController(
                this.heightProperty(),
                false
        );

        this.graveyardControllers.put(Colour.BLANC, graveyardBlanc);

        this.graveyardControllers.put(
                Colour.NOIR,
                new GraveyardController(
                        this.heightProperty(),
                        true,
                        this.heightProperty().add(graveyardBlanc.getLargeurTotale())
                )
        );

        //Crée une case pour chaque position
        PositionIterator positionIterator = new PositionIterator();

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            SquareLayout positionGraphique = new SquareLayout(position, this.heightProperty(), graveyardBlanc.getLargeurTotale());

            //Créer la case
            SquarePane squarePane = new SquarePane(
                    (position.getColonne() + position.getRangee()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    this::handleClick,
                    positionGraphique //La position de la case
            );

            //Ajouter la case à la liste
            cases.add(position, squarePane);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = gameData.getBoardMap().getPiece(position);

            if (piece != null) {
                PiecePane piecePane = new PiecePane(piece, positionGraphique);

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
        this.getChildren().addAll(cases.getValues());
        this.getChildren().addAll(piecePanes.values());

        this.gameData.setChangeListener(this::replacerLesPieces); // Si il y a un changement replacer les pièces

        game.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Game.Status.INACTIF) {
                if (animationController.getAnimationEnCours().get())
                    animationController.setOnFinishListener(game::notifierProchainJoueur);
                else game.notifierProchainJoueur();
            }
        });
    }

    /**
     * Appelé par un player pour demander au util d'enregistrer le moves du player humain
     *
     * @param moveRequest l'information sur le moves demandé
     */
    void demanderMouvement(MoveRequest moveRequest) {
        this.moveRequest = moveRequest;
    }

    /**
     * Quand une position est appuyé
     *
     * @param positionGraphique la position est appuyé
     */
    private void handleClick(Layout positionGraphique) {
        if (!(positionGraphique instanceof SquareLayout)) return;

        Position position = ((SquareLayout) positionGraphique).getPosition();

        //Si aucune moveRequest ne rien faire
        if (moveRequest == null || moveRequest.isCompleted()) return;

        //Obtenir la pièce à cette position
        Piece pieceClicked = gameData.getBoardMap().getPiece(position);

        //Si rien n'est sélectionné
        if (!highlightController.isSelected()) {
            //Si la pièce existe et elle est de la couleur du moveRequest
            if (pieceClicked != null && moveRequest.getColour() == pieceClicked.getColour()) {

                //Calculer les mouvements possibles
                Collection<Move> moves = gameData.filterOnlyLegal(pieceClicked.generateAllMoves(gameData.getBoardMap(), position), pieceClicked.getColour());

                highlightController.selectionner(position, moves); //Sélectionner
            }
        } else {
            //Si la case est une des options appliquer le movement
            if (highlightController.isMouvementPossible(position)) {
                Move moveChoisi = highlightController.getMouvementPossible(position);
                highlightController.deSelectionner();
                moveRequest.apply(moveChoisi);
            } else {
                highlightController.deSelectionner(); //Déselectionner tout
            }
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private synchronized void replacerLesPieces() {
//        while (animationController.getAnimationEnCours().get()) Thread.yield();

        for (Piece piece : gameData.getBoardMap().iteratePieces()) {
            Position position = gameData.getBoardMap().getPosition(piece);

            SquareLayout positionGraphique = new SquareLayout(position, this.heightProperty(), graveyardControllers.get(Colour.BLANC).getLargeurTotale());
            PiecePane piecePane = piecePanes.get(piece);

            if (!piecePane.isAtPosition(positionGraphique)) {
                animationController.ajouterAnimation(piecePane,
                        positionGraphique
                );
            }
        }

        for (Piece piece : gameData.getEatenPieces()) {
            PiecePane piecePane = piecePanes.get(piece);
            if (!graveyardControllers.get(piece.getColour()).isInGraveyard(piece)) {
                animationController.ajouterAnimation(
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