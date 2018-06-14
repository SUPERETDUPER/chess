/*
 * MIT License
 *
 * Copyright (c) 2018 Martin Staadecker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
 *
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

    public void add(@NotNull Position position, @NotNull T valeur) {
        data.add(getIndex(position), valeur);
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