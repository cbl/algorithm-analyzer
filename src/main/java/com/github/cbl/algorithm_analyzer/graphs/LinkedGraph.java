package com.github.cbl.algorithm_analyzer.graphs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.cbl.algorithm_analyzer.contracts.Graph;

public class LinkedGraph<V,E> implements Graph<V,E> {

    public record Edge<V,E>(V from, V to, E weight) {
        public static <V,E> Edge<V,E> of(V a, V b, E e) {
            return new Edge<>(a, b, e);
        }

        private Pair<V> toPair() {
            return new Pair<>(from, to);
        }
    };

    private record Pair<V>(V a, V b) {
        static <V> Pair<V> of(V a, V b) {
            return new Pair<>(a, b);
        }
    };

    private final Map<Pair<V>,E> map;

    public LinkedGraph() {
        map = new HashMap<>();
    }

    public LinkedGraph(Collection<Edge<V,E>> edges) {
        map = edges.stream().collect(Collectors.toMap(Edge::toPair, Edge::weight));
    }

    private LinkedGraph(Map<Pair<V>,E> map) {
        this.map = map;
    }

    @Override
    public Collection<V> getVertices() {
        return map.keySet().stream().flatMap(p -> Stream.of(p.a, p.b)).distinct().toList();
    }

    @Override
    public boolean hasEdge(V from, V to) {
        return map.containsKey(Pair.of(from, to));
    }

    @Override
    public Optional<E> getEdgeMaybe(V from, V to) {
        return Optional.ofNullable(map.get(Pair.of(from, to)));
    }

    @Override
    public int getVerticeCount() {
        return (int) map.keySet().stream().flatMap(p -> Stream.of(p.a, p.b)).distinct().count();
    }

    @Override
    public void setEdge(V from, V to, E e) {
        map.put(Pair.of(from, to), e);
    }

    @Override
    public void deleteEdge(V from, V to) {
       map.remove(Pair.of(from, to));
    }

    @Override
    public Graph<V,E> clone() {
        return new LinkedGraph<>(new HashMap<>(map));
    }
    
}
