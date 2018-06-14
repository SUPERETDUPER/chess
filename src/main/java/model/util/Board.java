package model.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A data structure that stores an object in each position (square) of the board.
 * These objects are accessed using a {@link Position} key.
 * <p>
 * Internally the objects are stored in a list
 *
 * @param <T> The object that is stored in the data structure
 */
public class Board<T> implements Iterable<T> {
    /**
     * The data
     */
    @NotNull
    private final List<T> data = new ArrayList<>(Position.LIMIT * Position.LIMIT);

    /**
     * @param position the position key
     * @return the object at this position
     */
    @NotNull
    public T get(@NotNull Position position) {
        return data.get(getIndex(position));
    }

    public void add(@NotNull Position position, @NotNull T value) {
        data.add(getIndex(position), value);
    }

    /**
     * Calculates the index of an item at this position
     */
    @Contract(pure = true)
    private int getIndex(Position position) {
        return Position.LIMIT * position.getRow() + position.getColumn();
    }

    /**
     * @return an iterator that loops through all the objects in the data structure. Uses the position iterator.
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final PositionIterator positionIterator = new PositionIterator();

            @Override
            public boolean hasNext() {
                return positionIterator.hasNext();
            }

            @NotNull
            @Override
            public T next() {
                return Board.this.get(positionIterator.next());
            }
        };
    }

    @NotNull
    public Collection<T> getData() {
        return data;
    }
}