package modele.moves;

import modele.plateau.Plateau;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Ce mouvement est un mouvement avec la fonctionnalité supplémentaire de te notifier que le mouvement est appliqué/undo
 * De plus, il permet de transférer un objet entre le appliquer et undo
 * <p>
 * Ex. Une fois que le pion bouge, il ne peut plus sauter 2 cases vers l'avant. Donc, il doit savoir quand il bouge.
 * Le pion peut envelopper son mouvement avec cet objet. Il sera ensuite notifié quand le mouvement a été appliqué.
 * Pour défaire le mouvement, le pion doit savoir si le mouvement à été le premier mouvement du pion.
 * Il utilise donc T pour passer un object qui lui donne cette information.
 *
 * @param <T> le type d'objet transféré entre le apply et undo
 */
public class MouvementNotifyWrapper<T> extends Move {
    /**
     * Le mouvement que l'objet contient
     */
    private final Move move;

    /**
     * La méthode qui est appelé quand le mouvement est appliqué.
     * Cette méthode retourne un résultat
     */
    private final Supplier<T> applyNotify;

    /**
     * La méthode qui est appelé quand le mouvement est défait.
     */
    private final Consumer<T> undoNotify;

    /**
     * Le résultat fourni par applyNotify
     */
    private T result;

    public MouvementNotifyWrapper(Move move, Supplier<T> applyNotify, Consumer<T> undoNotify) {
        super(move.depart, move.fin);
        this.move = move;
        this.applyNotify = applyNotify;
        this.undoNotify = undoNotify;
    }

    @Override
    public void appliquer(Plateau plateau) {
        move.appliquer(plateau);
        result = applyNotify.get();
    }

    @Override
    public void undo(Plateau plateau) {
        move.undo(plateau);
        undoNotify.accept(result);
    }

    @Override
    public int getValeur() {
        return move.getValeur();
    }
}
