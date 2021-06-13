package com.github.cbl.algorithm_analyzer.graphs;

import com.github.cbl.algorithm_analyzer.contracts.Graph;

/**
 * Adjacent matrix based graph for int-vertices and no edge weight
 *
 * <p>TODO: should take type parameters
 */
public class AdjacentMatrixGraph implements Graph<Integer, boolean[][]> {

    private boolean[][] matrix;
    private int verticeCount;

    public AdjacentMatrixGraph(int verticeCount) {
        this.verticeCount = verticeCount;
        matrix = new boolean[verticeCount][];
        for (int i = 0; i < verticeCount; i++) {
            matrix[i] = new boolean[verticeCount];
        }
    }

    public void setEdge(Integer from, Integer to) {
        if (from >= 0 && to >= 0 && from < verticeCount && to < verticeCount) {
            matrix[from][to] = true;
        }
    }

    public void deleteEdge(Integer from, Integer to) {
        if (from >= 0 && to >= 0 && from < verticeCount && to < verticeCount) {
            matrix[from][to] = false;
        }
    }

    @Override
    public boolean hasEdge(Integer from, Integer to) {
        return matrix[from][to];
    }

    @Override
    public int getVerticeCount() {
        return verticeCount;
    }

    public boolean[][] getEdges() {
        return matrix;
    }
}
