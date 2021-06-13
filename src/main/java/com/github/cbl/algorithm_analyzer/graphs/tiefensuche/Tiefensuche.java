package com.github.cbl.algorithm_analyzer.graphs.tiefensuche;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.StringJoiner;

public class Tiefensuche implements Algorithm<Event, Tiefensuche.Data> {

    public static record Data(Graph<Integer, boolean[][]> graph, String[] nodeNames) {}
    ;

    public static record FinalTimesEvent(Data data, int[] discovered, int[] finished)
            implements Event {
        @Override
        public String toString() {
            Object[][] table = new Object[3][];
            table[0] = new Object[discovered.length + 1];
            table[1] = new Object[discovered.length + 1];
            table[2] = new Object[discovered.length + 1];
            table[0][0] = "";
            table[1][0] = "Entdeckt";
            table[2][0] = "Fertig";

            for (int i = 0; i < discovered.length; i++) {
                table[0][i + 1] = data.nodeNames()[i];
                table[1][i + 1] = discovered[i];
                table[2][i + 1] = finished[i];
            }

            return TablePrinter.toString(table);
        }
    }
    ;

    public static record FinalEdgeTypesEvent(Data data, EdgeType[][] types) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            for (int u = 0; u < types.length; u++) {
                for (int j = 0; j < types.length; j++) {
                    if (data.graph().hasEdge(u, j)) {
                        sj.add(
                                "["
                                        + data.nodeNames()[u]
                                        + " -> "
                                        + data.nodeNames()[j]
                                        + "]: "
                                        + types[u][j]);
                    }
                }
            }

            return sj.toString();
        }
    }

    public void run(EventConsumer<Event> events, Data data) {
        Graph<Integer, boolean[][]> graph = data.graph();
        boolean known[] = new boolean[graph.getVerticeCount()];
        int previous[] = new int[graph.getVerticeCount()];
        int discovered[] = new int[graph.getVerticeCount()];
        int finished[] = new int[graph.getVerticeCount()];
        int time = 0;

        for (int u = 0; u < graph.getVerticeCount(); u++) {
            if (!known[u]) {
                time = this.expand(graph, u, discovered, finished, previous, known, time);
            }
        }

        events.accept(new FinalTimesEvent(data, discovered, finished));

        EdgeType[][] types = new EdgeType[graph.getVerticeCount()][];

        for (int u = 0; u < graph.getVerticeCount(); u++) {
            types[u] = new EdgeType[graph.getVerticeCount()];
            for (int j = 0; j < graph.getVerticeCount(); j++) {
                types[u][j] = this.getEdgeType(u, j, graph, discovered, finished, previous);
            }
        }

        events.accept(new FinalEdgeTypesEvent(data, types));
    }

    protected EdgeType getEdgeType(
            int from,
            int to,
            Graph<Integer, boolean[][]> graph,
            int[] discovered,
            int[] finished,
            int[] previous) {
        if (!graph.hasEdge(from, to)) {
            return EdgeType.KeineKante;
        }

        if (discovered[from] < discovered[to]
                && discovered[to] < finished[to]
                && finished[to] < finished[from]) {
            return previous[to] == from ? EdgeType.Baumkante : EdgeType.Vorwaertskante;
        } else if (discovered[to] < finished[to]
                && finished[to] < discovered[from]
                && discovered[from] < finished[from]) return EdgeType.Querkante;

        return EdgeType.Rueckwaertskante;
    }

    protected int expand(
            Graph<Integer, boolean[][]> graph,
            int u,
            int[] discovered,
            int[] finished,
            int[] previous,
            boolean[] known,
            int time) {
        known[u] = true;
        discovered[u] = ++time;
        for (int v = 0; v < graph.getVerticeCount(); v++) {
            if (known[v] || !graph.hasEdge(u, v)) continue;
            previous[v] = u;
            time = this.expand(graph, v, discovered, finished, previous, known, time);
        }
        finished[u] = ++time;

        return time;
    }
}
