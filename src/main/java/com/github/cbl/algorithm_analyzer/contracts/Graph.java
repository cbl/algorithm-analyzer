package com.github.cbl.algorithm_analyzer.contracts;

/**
 * @param <V> type of vertices
 * @param <E> type of edge metadata (e.g. weight)
 */
public interface Graph<V, E> {

    abstract boolean hasEdge(V from, V to);

    abstract int getVerticeCount();

    void setEdge(V from, V to);

    void deleteEdge(V from, V to);

    E getEdges();
}
