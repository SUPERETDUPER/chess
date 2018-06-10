package gui.jeu.board;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gui.jeu.board.placement.CasePosition;
import gui.jeu.board.placement.GraveyardController;
import gui.jeu.board.view.CasePane;
import gui.jeu.board.view.PiecePane;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.layout.Pane;
import modele.Couleur;
import modele.Jeu;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Piece;
import modele.plateau.Plateau;
import modele.plateau.Position;
import modele.plateau.PositionIterator;
import modele.plateau.Tableau;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Set;

/**
 * Controle le plateau de jeu
 */
public class PlateauPane extends Pane implements Plateau {

    /**
     * La liste de cases
     */
    @NotNull
    private final Tableau<CasePane> cases = new Tableau<>();

    /**
     * La liste de pièces
     */
    @NotNull
    private final BiMap<Position, PiecePane> piecePanes = HashBiMap.create(32);

    /**
     * La controlleur d'animation
     */
    @NotNull
    private final AnimationController animationController = new AnimationController();

    /**
     * Le plateau de jeu et les rois
     */
    @NotNull
    private final JeuData jeuData;

    @NotNull
    private final HighlightController highlightController = new HighlightController(cases);

    @NotNull
    private final EnumMap<Couleur, GraveyardController> graveyardControllers = new EnumMap<>(Couleur.class);

    //Objet qui spécifie si l'on veut obtenir des mouvements de l'utilisateur
    @Nullable
    private DemandeDeMouvement demandeDeMouvement;

    /**
     * @param jeu le plateau de jeu
     */
    public PlateauPane(@NotNull Jeu jeu) {
        this.jeuData = jeu.getJeuData();

        //Créer les graveyards et les ajouterPiece à la liste
        GraveyardController graveyardBlanc = new GraveyardController(
                this.heightProperty(),
                false
        );

        this.graveyardControllers.put(Couleur.BLANC, graveyardBlanc);

        this.graveyardControllers.put(
                Couleur.NOIR,
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

            CasePosition positionGraphique = new CasePosition(position, this.heightProperty(), graveyardBlanc.getLargeurTotale());

            //Créer la case
            CasePane casePane = new CasePane(
                    (position.getColonne() + position.getRangee()) % 2 == 0, //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                    this::handleClick,
                    positionGraphique //La position de la case
            );

            //Ajouter la case à la liste
            cases.add(position, casePane);

            //Si il y a une pièce à cette position créer une pièce
            Piece piece = jeuData.getPlateau().getPiece(position);

            if (piece != null) {
                ajouterPiece(piece, position);
            }
        }

        //Ajouter toutes pièces au plateau
        this.getChildren().addAll(cases.getValues());

        jeu.getMouvements().addListener(this::replacerLesPieces); // Si il y a un changement replacer les pièces
    }

    /**
     * Appelé par un joueur pour demander au plateau d'enregistrer le mouvement du joueur humain
     *
     * @param moveRequest l'information sur le mouvement demandé
     */
    public void demanderMouvement(DemandeDeMouvement moveRequest) {
        this.demandeDeMouvement = moveRequest;
    }

    /**
     * Quand une position est appuyé
     *
     * @param position la position est appuyé
     */
    private void handleClick(Position position) {
        //Si aucune demandeDeMouvement ne rien faire
        if (demandeDeMouvement == null || demandeDeMouvement.isCompleted()) return;

        //Obtenir la pièce à cette position
        Piece pieceClicked = jeuData.getPlateau().getPiece(position);

        //Si rien n'est sélectionné
        if (!highlightController.isSelected()) {
            //Si la pièce existe et elle est de la couleur du demandeDeMouvement
            if (pieceClicked != null && demandeDeMouvement.getCouleur() == pieceClicked.getCouleur()) {

                //Calculer les mouvements possibles
                Set<Mouvement> moves = jeuData.filterOnlyLegal(pieceClicked.generateAllMoves(jeuData.getPlateau()), pieceClicked.getCouleur());

                highlightController.selectionner(position, moves); //Sélectionner
            }
        } else {
            //Si la case est une des options appliquer le movement
            if (highlightController.isMouvementPossible(position)) {
                Mouvement mouvementChoisi = highlightController.getMouvementPossible(position);
                highlightController.deSelectionner();
                demandeDeMouvement.apply(mouvementChoisi);
            } else {
                highlightController.deSelectionner(); //Déselectionner tout
            }
        }
    }

    /**
     * Pour chaque case afficher la pièce à cette case
     */
    private void replacerLesPieces(ListChangeListener.Change<? extends Mouvement> change) {
        while (change.next()) {
            for (Mouvement undid : change.getRemoved()) {
                System.out.println("undo: " + undid.toString());
            }
            for (Mouvement applied : change.getAddedSubList()) {
                applied.appliquer(this);
                System.out.println("apply: " + applied.toString());
            }
        }
    }

    @Override
    public void ajouterPiece(Piece piece, Position position) {
        //TODO check if in graveyard
        PiecePane piecePane = new PiecePane(piece,
                new CasePosition(
                        position,
                        this.heightProperty(),
                        this.graveyardControllers.get(Couleur.BLANC).getLargeurTotale()
                )
        );

        //Ajouter les listeners
        piecePane.getPane().setOnMousePressed(event -> handleClick(jeuData.getPlateau().getPosition(piece)));

        //Ajouter la pièce à la liste de pièce
        piecePanes.put(position, piecePane);
        this.getChildren().add(piecePane.getPane());
    }

    @Override
    public Piece bougerPiece(Piece piece, Position finale) {
        Piece pieceAFin = piecePanes.get(finale);

        animationController.ajouterAnimation(piecePanes.get(piecePanes.inverse().get(pieceAFin)), new CasePosition(
                finale,
                this.heightProperty(),
                graveyardControllers.get(Couleur.BLANC).getLargeurTotale()
        ));

        return pieceAFin;
    }

    @Override
    public Position mangerPiece(Piece piece) {
        animationController.ajouterAnimation(
                piecePanes.get(piecePanes.inverse().get(piece)),
                graveyardControllers.get(piece.getCouleur()).getNextGraveyardPosition()
        );

        return piecePanes.inverse().remove(piece);
    }

    @Override
    public Piece mangerPiece(Position position) {
        return null;
    }

    @Override
    public Position eneleverPiece(Piece piece) {
        return null;
    }

    @Override
    public Piece eneleverPiece(Position position) {
        return null;
    }

    @Override
    public Position getPosition(Piece piece) {
        return null;
    }

    //    //Pour chaque pieces déplacer à position
//        for(
//    int i = 0; i<piecePanes.size();i++)
//
//    {
//        PiecePane piecePane = piecePanes.get(i);
//
//        if (piecePane != null) {
//            Position position = plateau.getPosition(piecePane.getPiece());
//
//            //Si la position existe déplacer à position
//            //Si la position n'existe pas déplacer à graveyard
//            if (position == null) {
//                animationController.ajouterAnimation(
//                        piecePane,
//                        graveyardControllers.get(piecePane.getPiece().getCouleur()).getNextGraveyardPosition()
//                );
//
//                piecePanes.set(i, null);
//            } else {
//                animationController.ajouterAnimation(
//                        piecePane,
//                        new CasePosition(position, this.heightProperty(), graveyardControllers.get(Couleur.BLANC).getLargeurTotale())
//                );
//            }
//        }
//    }

//}

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