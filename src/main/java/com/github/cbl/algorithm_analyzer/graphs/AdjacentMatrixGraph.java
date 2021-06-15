package com.github.cbl.algorithm_analyzer.graphs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.IntStream;

import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.contracts.WeightFreeGraph;

/** Adjacent matrix based graph for int-vertices and no edge weight */
public class AdjacentMatrixGraph implements WeightFreeGraph<Integer> {

    private final boolean[][] matrix;
    private final int verticeCount;

    public AdjacentMatrixGraph(int verticeCount) {
        this.verticeCount = verticeCount;
        matrix = new boolean[verticeCount][];
        for (int i = 0; i < verticeCount; i++) {
            matrix[i] = new boolean[verticeCount];
        }
    }

    private AdjacentMatrixGraph(int verticeCount, boolean[][] matrix) {
        this.verticeCount = verticeCount;
        this.matrix = matrix;
    }

    @Override
    public Collection<Integer> getVertices() {
        return Arrays.asList(IntStream.range(0, verticeCount).mapToObj(Integer::valueOf).toArray(Integer[]::new));
    }

    @Override
    public Optional<Boolean> getEdgeMaybe(Integer from, Integer to) {
        var e = matrix[from][to];
        return Optional.ofNullable(e);
    }

    public void setEdge(Integer from, Integer to, Boolean weight) {
        if (from >= 0 && to >= 0 && from < verticeCount && to < verticeCount) {
            matrix[from][to] = weight;
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

    @Override
    public Graph<Integer,Boolean> clone() {
        var matrixClone = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            matrixClone[i] = matrixClone[i].clone();
        }
        return new AdjacentMatrixGraph(verticeCount, matrixClone);
    }
}
