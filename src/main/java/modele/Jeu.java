package modele;

import modele.board.Board;
import modele.joueur.Joueur;
import modele.moves.Move;
import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class Jeu {
    @NotNull
    private final Board board;

    @NotNull
    private final EnumMap<Couleur, Joueur> joueurs = new EnumMap<>(Couleur.class);

    @NotNull
    private final EnumMap<Couleur, Roi> rois = new EnumMap<>(Couleur.class);

    private Couleur currentPlayerCouleur = Couleur.BLANC;

    public Jeu(@NotNull Board board, @NotNull Roi premierRoi, @NotNull Roi deuxiemeRoi) {
        this.board = board;

        rois.put(premierRoi.getCouleur(), premierRoi);
        rois.put(deuxiemeRoi.getCouleur(), deuxiemeRoi);
    }

    public boolean roiInCheck(Couleur couleurDuRoi) {
        for (Piece piece : board.iteratePieces()) {
            if (piece.getCouleur() != couleurDuRoi && piece.attacksPosition(board, board.getPosition(rois.get(couleurDuRoi)))) {
                return false;
            }
        }

        return true;
    }

    @NotNull
    public static Set<Move> getAllLegalMoves(Jeu jeu, Couleur couleur) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : jeu.getBoard().iteratePieces()) {
            if (piece.getCouleur() == couleur) {
                moves.addAll(piece.getLegalMoves(jeu));
            }
        }

        return moves;
    }

    public void ajouterJoueur(@NotNull Joueur joueur) {
        joueurs.put(joueur.getCouleur(), joueur);
    }

    public void commencer() {
        joueurs.get(currentPlayerCouleur).notifierTour(new MoveCallbackWrapper(this::jouer));
    }

    public void jouer(Move move) {
        move.apply(board); //Jouer
        currentPlayerCouleur = currentPlayerCouleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC; //Changer le tour

        Set<Move> moves = getAllLegalMoves(this, currentPlayerCouleur);

        if (moves.isEmpty()) {
            if (roiInCheck(currentPlayerCouleur)) {
                System.out.println("Stalemate");
            } else {
                System.out.println("Checkmate");
            }
        }

        joueurs.get(currentPlayerCouleur).notifierTour(new MoveCallbackWrapper(this::jouer)); //Notifier l'autre joueur
    }


    @NotNull
    public Board getBoard() {
        return board;
    }
}