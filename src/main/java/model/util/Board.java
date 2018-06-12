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
 * Une structure de données pour le util du gamewindow d'échec
 * Permet aux autres class de garder un objet dans une position du tableau.
 * <p>
 * Les classes peuvent accéder les données dans la structure à l'aide de {@link Position} sans devoir se soucier de comment les données sont organisées
 * <p>
 * À l'interne, la structure de données une liste qui stocke toutes les données en ordre de gauche à droite puis de haut en bas
 *
 * @param <T> Le type de donnée stockée
 */
public class Board<T> implements Iterable<T> {
    /**
     * La liste contenant les données
     */
    @NotNull
    private final List<T> liste = new ArrayList<>(Position.LIMITE * Position.LIMITE);

    /**
     * @param position la position de la donnée désirée
     * @return la donnée désirée
     */
    @NotNull
    public T get(@NotNull Position position) {
        return liste.get(getIndex(position));
    }

    public void add(@NotNull Position position, @NotNull T valeur) {
        liste.add(getIndex(position), valeur);
    }

    @Contract(pure = true)
    private int getIndex(Position position) {
        return getIndex(position.getRangee(), position.getColonne());
    }

    @Contract(pure = true)
    private int getIndex(int rangee, int colonne) {
        return Position.LIMITE * rangee + colonne;
    }

    /**
     * @return un iterator qui passe sur toutes les position de la structure de données
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

            @Override
            public T next() {
                return Board.this.get(positionIterator.next());
            }
        };
    }

    public Collection<T> getValues() {
        return liste;
    }
}