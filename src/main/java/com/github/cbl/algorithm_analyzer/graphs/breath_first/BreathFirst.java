package com.github.cbl.algorithm_analyzer.graphs.breath_first;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.contracts.Graph.NoEdgeException;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;

import java.util.StringJoiner;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class BreathFirst<V> implements Algorithm<Event, BreathFirst.Data<V>> {

    private static final int INFINITY = Integer.MAX_VALUE;

    public static record Data<V>(Graph<V, Integer> graph, V start) {}
    ;

    public static record FinalTimesEvent<V>(
            Graph<V, Integer> graph, State<V> state, V start) implements Event {
        @Override
        public String toString() {

            Object[][] table = new Object[4][];
            table[0] = new Object[graph.getVerticeCount() + 1];
            table[1] = new Object[graph.getVerticeCount() + 1];
            table[2] = new Object[graph.getVerticeCount() + 1];
            table[3] = new Object[graph.getVerticeCount() + 1];
            table[0][0] = "";
            table[1][0] = "Is known";
            table[2][0] = "Distance";
            table[3][0] = "Previous";

            int i = 1;
            for (V v : graph.getVertices().stream().sorted().toList()) {
                table[0][i] = v.toString();
                table[1][i] = state.isKnown.get(v);
                table[2][i] = state.distance.get(v) == INFINITY ? "∞" : state.distance.get(v);
                table[3][i] = state.previous.get(v);
                i++;
            }

            return TablePrinter.toString(table);
        }
    }
    ;

    private record State<V>(Map<V, Boolean> isKnown, Map<V, Integer> distance, Map<V, V> previous)
            implements Cloneable {

        public State() {
            this(new HashMap<V, Boolean>(), new HashMap<V, Integer>(), new HashMap<V, V>());
        }

        @Override
        public State<V> clone() {
            return new State<>(
                    new HashMap<V, Boolean>(isKnown),
                    new HashMap<V, Integer>(distance),
                    new HashMap<V, V>(previous));
        }
    }

    public void run(EventConsumer<Event> events, Data<V> data) {
        Graph<V, Integer> g = data.graph();
        V start = data.start();
        State<V> state = new State<V>();
        
        for(V u : g.getVertices()) {
            if(u == start) continue;
            state.isKnown.put(u, false);
            state.distance.put(u, INFINITY);
            state.previous.put(u, null);
        }

        state.distance.put(start, 0);
        state.isKnown.put(start, true);

        Queue<V> q = new LinkedList<V>();
        q.add(start);

        try {
            while(!q.isEmpty()) {
                V u = q.poll();
                for(V v : g.getVertices().stream().sorted().toList()) {
                    if(!g.hasEdge(u, v)) continue;
                    if(!state.isKnown.get(v)) {
                        state.distance.put(v, state.distance.get(u)+g.getEdge(u, v));
                        state.previous.put(v, u);
                        state.isKnown.put(v, true);
                        q.add(v);
                    }
                }
            }
            events.accept(new FinalTimesEvent(g, state.clone(), start));
        } catch(NoEdgeException e) {
            System.out.println("Error: Missing edge");
        }
    }
}
