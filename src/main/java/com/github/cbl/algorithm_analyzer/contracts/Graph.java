package com.github.cbl.algorithm_analyzer.contracts;

import java.util.Collection;
import java.util.Optional;

/**
 * @param <V> type of vertices
 * @param <E> type of edge metadata (e.g. weight)
 */
public interface Graph<V, E> extends Cloneable {

    class NoEdgeException extends Exception {}

    boolean hasEdge(V from, V to);

    default E getEdge(V from, V to) throws NoEdgeException {
        return getEdgeMaybe(from, to).orElseThrow(NoEdgeException::new);
    }

    Optional<E> getEdgeMaybe(V from, V to);

    int getVerticeCount();

    Collection<V> getVertices();

    void setEdge(V from, V to, E e);

    void deleteEdge(V from, V to);

    Graph<V, E> clone();
}
