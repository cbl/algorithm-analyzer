package com.github.cbl.algorithm_analyzer.graphs;

import com.github.cbl.algorithm_analyzer.contracts.Graph;

/**
 * Adjacent matrix based graph for int-vertices and no edge weight
 * TODO: should take type parameters
 */
public class AdjacentMatrixGraph implements Graph<Integer,Void> {

    private boolean[][] adj;
    private int verticeCount;

    public AdjacentMatrixGraph(int verticeCount) {
        this.verticeCount = verticeCount;
        adj = new boolean[verticeCount][];
        for (int i = 0; i < verticeCount; i++)
            adj[i] = new boolean[verticeCount];
    }

    public void setEdge(Integer from, Integer to) {
        if (from >= 0 && to >= 0 && from < verticeCount && to < verticeCount)
            adj[from][to] = true;
    }

    public void deleteEdge(Integer from, Integer to) {
        if (from >= 0 && to >= 0 && from < verticeCount && to < verticeCount)
            adj[from][to] = false;
    }

    @Override
    public boolean hasEdge(Integer from, Integer to) {
        return adj[from][to];
    }

    @Override
    public int getVerticeCount() {
        return verticeCount;
    }
}
