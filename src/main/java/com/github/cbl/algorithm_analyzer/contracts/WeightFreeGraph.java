package com.github.cbl.algorithm_analyzer.contracts;

/**
 * @param <V> type of vertices
 * @param <E> type of edge metadata (e.g. weight)
 */
public interface WeightFreeGraph<V> extends Graph<V,Boolean> {

    default void setEdge(V from, V to) {
        setEdge(from, to, true);
    }

}
