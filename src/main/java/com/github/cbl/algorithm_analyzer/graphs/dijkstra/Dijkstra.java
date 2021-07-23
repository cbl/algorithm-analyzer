package com.github.cbl.algorithm_analyzer.graphs.dijkstra;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.graphs.AdjacentMatrixGraph;
import com.github.cbl.algorithm_analyzer.util.TablePrinter;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;

import java.util.HashMap;
import java.util.Map;

public class Dijkstra<V> implements Algorithm<Event, Dijkstra.Data> {

    private static final int INFINITY = Integer.MAX_VALUE;

    public record Data<V>(Graph<V, Integer> graph, V start) {}

    public class PartialDistancesEvent implements Event {
        Data data;
        State state;
        int i;

        PartialDistancesEvent(Data data, State state, int i) {
            this.data = data;
            this.state = state;
            this.i = i;
        }
        
        @Override
        public String toString() {
            String[][] table = new String[4][];
            Graph<V, Integer> g = data.graph;
            table[0] = new String[g.getVerticeCount()+2];
            table[1] = new String[g.getVerticeCount()+2];
            table[2] = new String[g.getVerticeCount()+2];
            table[3] = new String[g.getVerticeCount()+2];
            table[0][0] = "Iteration";
            table[1][0] = Integer.toString(i);
            table[1][1] = "Cost";
            table[2][1] = "Previous Node";
            table[3][1] = "Done";
            int j=0;
            for(V v : g.getVertices()) {
                table[0][2+j] = v.toString();
                table[1][2+j] = state.distance.get(v) == INFINITY ? "∞" : state.distance.get(v).toString();
                table[2][2+j] = state.previous.get(v) == null ? "" : state.previous.get(v).toString();
                table[3][2+j] = state.done.get(v) ? "✓" : "";
                j++;
            }
            return TablePrinter.toString(table);
        }  
    };

    private class State implements Cloneable {
        Map<V, Integer> distance;
        Map<V, V> previous;
        Map<V, Boolean> done;

        public State() {
            this.distance = new HashMap<V, Integer>();
            this.previous = new HashMap<V, V>();
            this.done = new HashMap<V, Boolean>();
        }

        private State(Map<V, Integer> distance, Map<V, V> previous, Map<V, Boolean> done) {
            this.distance = distance;
            this.previous = previous;
            this.done = done;
        }

        @Override
        public State clone() {
            return new State(
                new HashMap<V, Integer>(this.distance), 
                new HashMap<V, V>(this.previous), 
                new HashMap<V, Boolean>(this.done)
            );
        }
    }

    @Override
    public void run(EventConsumer<Event> events, Data data) {
        Graph<V, Integer> g = data.graph;
        State state = new State();
        V next;
        int i = 1;

        for(V u : g.getVertices()) {
            state.distance.put(u, INFINITY);
            state.previous.put(u, null);
            state.done.put(u, false);
        }
        state.distance.put((V) data.start, 0);
        while(verticeExists(data, state)) {
            next = getNextVertice(data, state);
            state.done.put(next, true);
            for(V v : g.getVertices()) {
                if(!g.hasEdge(next, v)) continue;

                try {
                    if(state.distance.get(v) > (state.distance.get(next) + g.getEdge(next, v))) {
                        state.distance.put(v, state.distance.get(next) + g.getEdge(next, v));
                        state.previous.put(v, next);
                    }
                } catch(Graph.NoEdgeException e) {
                    e.printStackTrace();
                    assert (false); // should never happen. If the distances graph misses some edges there
                    // is a conceptional error, no runtime error
                }
            }
            events.accept(new PartialDistancesEvent(data, state.clone(), i++));
        }
    }

    public boolean verticeExists(Data data, State state)
    {
        Graph<V, Integer> g = data.graph;
        for(V v : g.getVertices()) {
            if(state.distance.get(v) != INFINITY && !state.done.get(v)) {
                return true;
            }
        }

        return false;
    }

    public V getNextVertice(Data data, State state) {
        Graph<V, Integer> g = data.graph;
        Integer minimum = INFINITY;
        V next = null;
        for(V v : g.getVertices()) {
            if(!state.done.get(v) && state.distance.get(v) < minimum) {
                minimum = state.distance.get(v);
                next = v;
            }
        }

        return next;
    }
}