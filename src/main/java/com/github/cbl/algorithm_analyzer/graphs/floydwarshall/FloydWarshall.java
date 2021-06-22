package com.github.cbl.algorithm_analyzer.graphs.floydwarshall;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.contracts.Graph.NoEdgeException;
import com.github.cbl.algorithm_analyzer.graphs.LinkedGraph;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Calculates all shortest path between all node pairs of a weighted graph */
public class FloydWarshall<V> implements Algorithm<Event, FloydWarshall.Data<V>> {

    public record Data<V>(Graph<V, Integer> costs) {}

    record PartialDistancesEvent<V>(Graph<V, Distance<V>> distances, Collection<V> nodes)
            implements Event {
        @Override
        public String toString() {
            try {
                // convert distances graph to (string)[][] in order to print as table
                var vn = distances.getVerticeCount();
                var vs =
                        StreamSupport.stream(distances.getVertices().spliterator(), false).toList();
                String[][] ss = new String[vn + 1][vn + 1];
                ss[0][0] = "";
                for (int i = 0; i < vn; i++) {
                    ss[0][i + 1] = vs.get(i).toString();
                    ss[i + 1][0] = vs.get(i).toString();
                }
                for (int i = 0; i < vn; i++) {
                    for (int j = 0; j < vn; j++) {
                        ss[i + 1][j + 1] = distances.getEdge(vs.get(i), (V) vs.get(j)).toString();
                    }
                }
                return "Nodes: " + nodes.toString() + "\n" + TablePrinter.toString(ss);
            } catch (NoEdgeException e) {
                e.printStackTrace();
                return "Error: table of distances if only partially filled";
            }
        }
    }

    record PathEvent<V>(List<V> path, V from, V to) implements Event {
        @Override
        public String toString() {
            return "Shorted path from " + from + " to " + to + " is over: " + path.toString();
        }
    }

    /** Current distance between to nodes and the (optional) node over which the path leads */
    private record Distance<V>(Integer distance, V over) implements Comparable<Distance<V>> {

        static <V> Distance<V> infinite() {
            return new Distance<>(null, null);
        }

        Distance<V> add(Distance<V> other) {
            if (null == this.distance || null == other.distance) {
                return Distance.infinite();
            } else {
                return new Distance<>(this.distance + other.distance, null);
            }
        }

        @Override
        public String toString() {
            if (null == distance) {
                return "-";
            } else if (null == over) {
                return distance.toString();
            } else {
                return distance + ":" + over;
            }
        }

        @Override
        public int compareTo(Distance<V> o) {
            if (null == this.distance) {
                if (null == o.distance) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                if (null == o.distance) {
                    return -1;
                } else {
                    return this.distance - o.distance;
                }
            }
        }
    }

    @Override
    public void run(EventConsumer<Event> events, Data<V> data) {
        final Graph<V, Distance<V>> distances = new LinkedGraph<>();
        for (V i : data.costs.getVertices()) {
            for (V j : data.costs.getVertices()) {
                if (i.equals(j)) {
                    distances.setEdge(i, j, new Distance<>(0, null));
                } else {
                    distances.setEdge(
                            i,
                            j,
                            data.costs
                                    .getEdgeMaybe(i, j)
                                    .map(cost -> new Distance<V>(cost, null))
                                    .orElseGet(Distance::infinite));
                }
            }
        }

        events.accept(new PartialDistancesEvent<>(distances.clone(), Set.of()));

        try {
            Set<V> nodes = new HashSet<>();
            for (V k : data.costs.getVertices()) {
                nodes.add(k);
                for (V i : data.costs.getVertices()) {
                    for (V j : data.costs.getVertices()) {
                        var d = distances.getEdge(i, j);
                        var dk = distances.getEdge(i, k).add(distances.getEdge(k, j));
                        if (dk.compareTo(d) < 0) {
                            distances.setEdge(i, j, new Distance<>(dk.distance, k));
                        }
                    }
                }

                events.accept(new PartialDistancesEvent<>(distances.clone(), new HashSet<>(nodes)));
            }

            for (V i : data.costs.getVertices()) {
                for (V j : data.costs.getVertices()) {
                    if (!i.equals(j)) {
                        var p = path(distances, i, j);
                        events.accept(new PathEvent<>(p, i, j));
                    }
                }
            }
        } catch (NoEdgeException e) {
            e.printStackTrace();
            assert (false); // should never happen. If the distances graph misses some edges there
                            // is a conceptional error, no runtime error
        }
    }

    private List<V> path(Graph<V, Distance<V>> distances, V from, V to) throws NoEdgeException {
        var d = distances.getEdge(from, to);
        if (null == d.over) {
            return List.of();
        } else {
            var a = path(distances, from, d.over);
            var b = path(distances, d.over, to);

            return Stream.concat(Stream.concat(a.stream(), Stream.of(d.over)), b.stream()).toList();
        }
    }
}
