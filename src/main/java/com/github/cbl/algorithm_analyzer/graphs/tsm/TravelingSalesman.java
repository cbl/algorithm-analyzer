package com.github.cbl.algorithm_analyzer.graphs.tsm;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.contracts.Graph.NoEdgeException;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TravelingSalesman<V> implements Algorithm<Event, TravelingSalesman.Data<V>> {
    public record Data<V>(Graph<V, Integer> graph) {}

    public record PartialRoundTripEvent<V>(List<V> roundTrip) implements Event {
        @Override
        public String toString() {
            return ArrayPrinter.toString(roundTrip.toArray());
        }
    }

    public record FinalCostEvent<V>(Data<V> data, int[][] cost, V[] rout) implements Event {

        @Override
        public String toString() {
            Graph<V, Integer> g = data.graph;
            String[][] table = new String[data.graph.getVerticeCount() + 1][];
            for (int j = 0; j < table.length; j++) table[j] = new String[g.getVerticeCount() + 1];
            int row = 1;
            int col = 0;
            for (V v : g.getVertices().stream().sorted().toList()) {
                table[row][0] = v.toString();
                if (col != 0) {
                    table[0][col] = v.toString();
                }
                for (int c = 1; c < cost[row - 1].length; c++) {
                    table[row][c] = Integer.toString(cost[row - 1][c]);
                    if (c - 1 == row - 1 && c > 1) {
                        table[row][c] += " (" + rout[c - 1].toString() + ")";
                    }
                }
                row++;
                col++;
            }
            return TablePrinter.toString(table);
        }
    }

    @Override
    public void run(EventConsumer<Event> events, Data<V> data) {
        Graph<V, Integer> g = data.graph;
        Object[] v = new Object[g.getVerticeCount()];
        int n = v.length;
        int x = 0;
        for (V u : g.getVertices().stream().sorted().toList()) v[x++] = u;
        Object[] rout = new Object[g.getVerticeCount()];
        int[][] len = new int[g.getVerticeCount()][];
        for (int i = 0; i < v.length; i++) len[i] = new int[g.getVerticeCount() + 1];

        try {
            for (int i = 0; i < (n - 1); i++) {
                if (i == 0) {
                    len[0][1] = g.getEdge((V) v[0], (V) v[1]);
                } else {
                    int min = Integer.MAX_VALUE;
                    int minK = 0;
                    for (int k = 0; k < i; k++) {
                        int value = len[k][i] + g.getEdge((V) v[k], (V) v[i + 1]);
                        if (value < min) {
                            minK = k;
                            min = value;
                        }
                    }
                    len[i][i + 1] = min;
                    rout[i] = v[minK];
                }

                for (int j = i + 2; j <= n; j++) {
                    if (j == n) {
                        len[i][j] = len[i][j - 1] + g.getEdge((V) v[i], (V) v[j - 1]);
                    } else {
                        len[i][j] = len[i][j - 1] + g.getEdge((V) v[j - 1], (V) v[j]);
                    }
                }
            }

            int min = Integer.MAX_VALUE;
            int minK = 0;
            for (int k = 0; k < n - 1; k++) {
                int value = len[k][n - 1] + g.getEdge((V) v[k], (V) v[n - 1]);
                if (value < min) {
                    minK = k;
                    min = value;
                }
            }
            len[v.length - 1][v.length] = min;
            rout[v.length - 1] = v[minK];
            events.accept(new FinalCostEvent<V>(data, deepClone(len), (V[]) rout));
        } catch (NoEdgeException e) {
            System.out.println("Missing edge.");
        }
        roundTrip(events, data, (V[]) rout);
    }

    protected void roundTrip(EventConsumer<Event> events, Data<V> data, V[] rout) {
        Graph<V, Integer> g = data.graph;
        List<V> roundTrip = new ArrayList<V>();
        Object[] v = new Object[g.getVerticeCount()];
        int x = 0;
        for (V u : g.getVertices().stream().sorted().toList()) v[x++] = u;
        int n = g.getVerticeCount();
        int i = n - 1;
        for (int j = n - 1; j >= 0; j--) {
            if (i == j) {
                Collections.reverse(roundTrip);
                i = indexOf((V[]) v, rout[j]);
            }
            roundTrip.add((V) v[j]);
            events.accept(new PartialRoundTripEvent<V>(new ArrayList<V>(roundTrip)));
        }

        events.accept(new PartialRoundTripEvent<V>(new ArrayList<V>(roundTrip)));
    }

    protected int indexOf(V[] v, V e) {
        for (int i = 0; i < v.length; i++) {
            if (e == v[i]) {
                return i;
            }
        }
        return -1;
    }

    protected void invert(Object[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            Object temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    public static int[][] deepClone(int[][] input) {
        if (input == null) return null;
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
