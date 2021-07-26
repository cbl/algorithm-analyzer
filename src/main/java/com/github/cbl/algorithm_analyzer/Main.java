package com.github.cbl.algorithm_analyzer;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.contracts.WeightFreeGraph;
import com.github.cbl.algorithm_analyzer.graphs.AdjacentMatrixGraph;
import com.github.cbl.algorithm_analyzer.graphs.LinkedGraph;
import com.github.cbl.algorithm_analyzer.graphs.LinkedGraph.Edge;
import com.github.cbl.algorithm_analyzer.graphs.deepsearch.Deepsearch;
import com.github.cbl.algorithm_analyzer.graphs.dijkstra.Dijkstra;
import com.github.cbl.algorithm_analyzer.graphs.floydwarshall.FloydWarshall;
import com.github.cbl.algorithm_analyzer.graphs.tsm.TravelingSalesman;
import com.github.cbl.algorithm_analyzer.hashing.CoalescedHashTable;
import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.sorts.heapsort.HeapSort;
import com.github.cbl.algorithm_analyzer.sorts.quicksort.Quicksort;
import com.github.cbl.algorithm_analyzer.sorts.shellsort.Shellsort;
import com.github.cbl.algorithm_analyzer.trees.AvlTree.AVLTree;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        // Main.tsm();
        Main.coalescedHashTable();
    }

    public static void coalescedHashTable()
    {
        int mod = 10;
        int reserved = 2;
        EventConsumer<Event> ec = new GeneralEventConsumer();
        CoalescedHashTable table = new CoalescedHashTable(ec, mod, reserved);

        table.insert(29);
        table.insert(12);
        table.insert(7);
        table.insert(19);
        table.insert(30);
        table.insert(40);
        table.insert(2);
        table.insert(39);
        table.insert(8);

        ec.visitEvents(new LogEventVisitor());
    }

    public static void tsm() {
        Collection<Edge<Character, Integer>> edges =
                Set.of(
                        Edge.of('A', 'B', 7),
                        Edge.of('A', 'C', 5),
                        Edge.of('A', 'D', 8),
                        Edge.of('A', 'E', 12),
                        Edge.of('B', 'C', 5),
                        Edge.of('B', 'D', 9),
                        Edge.of('B', 'E', 8),
                        Edge.of('C', 'D', 4),
                        Edge.of('C', 'E', 7),
                        Edge.of('D', 'E', 9));
        Graph<Character, Integer> graph = new LinkedGraph<>(edges);
        for (Edge<Character, Integer> e : edges) {
            graph.setEdge(e.to(), e.from(), e.weight());
        }

        final Algorithm<Event, TravelingSalesman.Data<Character>> a = new TravelingSalesman<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new TravelingSalesman.Data<Character>(graph));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void tsm2() {
        Collection<Edge<Integer, Integer>> edges =
                Set.of(
                        Edge.of(0, 1, 5),
                        Edge.of(0, 2, 2),
                        Edge.of(0, 3, 3),
                        Edge.of(0, 4, 5),
                        Edge.of(0, 5, 5),
                        Edge.of(0, 6, 6),
                        Edge.of(0, 7, 8),
                        Edge.of(1, 2, 3),
                        Edge.of(1, 3, 6),
                        Edge.of(1, 4, 10),
                        Edge.of(1, 5, 8),
                        Edge.of(1, 6, 6),
                        Edge.of(1, 7, 10),
                        Edge.of(2, 3, 3),
                        Edge.of(2, 4, 6),
                        Edge.of(2, 5, 5),
                        Edge.of(2, 6, 4),
                        Edge.of(2, 7, 7),
                        Edge.of(3, 4, 3),
                        Edge.of(3, 5, 2),
                        Edge.of(3, 6, 4),
                        Edge.of(3, 7, 5),
                        Edge.of(4, 5, 2),
                        Edge.of(4, 6, 6),
                        Edge.of(4, 7, 3),
                        Edge.of(5, 6, 4),
                        Edge.of(5, 7, 2),
                        Edge.of(6, 7, 4));
        Graph<Integer, Integer> graph = new LinkedGraph<>(edges);
        for (Edge<Integer, Integer> e : edges) {
            graph.setEdge(e.to(), e.from(), e.weight());
        }

        final Algorithm<Event, TravelingSalesman.Data<Integer>> a = new TravelingSalesman<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new TravelingSalesman.Data<Integer>(graph));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void dijkstra() {
        Graph<Character, Integer> costs =
                new LinkedGraph<>(
                        Set.of(
                                Edge.of('A', 'B', 100),
                                Edge.of('A', 'C', 50),
                                Edge.of('B', 'C', 100),
                                Edge.of('B', 'D', 100),
                                Edge.of('B', 'E', 250),
                                Edge.of('C', 'E', 250),
                                Edge.of('D', 'E', 50)));

        final Algorithm<Event, Dijkstra.Data<Character>> a = new Dijkstra<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Dijkstra.Data<Character>(costs, 'A'));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void tiefenSuche() {
        int size = 4;
        String[] nodeNames = {"A", "B", "C", "D", "E"};
        WeightFreeGraph<Integer> graph = new AdjacentMatrixGraph(size);

        graph.setEdge(0, 3);
        graph.setEdge(0, 2);
        graph.setEdge(1, 3);
        graph.setEdge(2, 1);
        graph.setEdge(3, 2);

        final Algorithm<Event, Deepsearch.Data> a = new Deepsearch();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Deepsearch.Data(graph, nodeNames));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void bubbleSort() {
        final Integer[] array = {6, 5, 4, 3, 2, 1};

        final Algorithm<Event, BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new BubbleSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void shellSort() {
        final Integer[] array = {6, 5, 4, 3, 2, 1};

        final Algorithm<Event, Shellsort.Data<Integer>> a = new Shellsort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Shellsort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void quickSort() {
        final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

        final Algorithm<Event, Quicksort.Data<Integer>> a = new Quicksort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Quicksort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void heapSort() {
        final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

        final Algorithm<Event, HeapSort.Data<Integer>> a = new HeapSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new HeapSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void avlTree() {
        AVLTree<Integer> t = new AVLTree<Integer>(Comparator.naturalOrder());
        EventConsumer<Event> ec = new GeneralEventConsumer();
        t.onEvent(ec);

        t.insert(4);
        t.insert(2);
        t.insert(5);
        t.insert(1);
        t.insert(3);
        t.insert(6);
        t.insert(10);
        t.insert(15);
        t.remove(2);
        t.insert(13);
        t.remove(3);

        ec.visitEvents(new LogEventVisitor());
    }

    @SuppressWarnings("varargs")
    public static void floydWarshall() {
        Graph<Character, Integer> costs =
                new LinkedGraph<>(
                        Set.of(
                                Edge.of('v', 'x', 2),
                                Edge.of('w', 'v', 10),
                                Edge.of('w', 'x', 5),
                                Edge.of('x', 'v', 3),
                                Edge.of('x', 'y', 2),
                                Edge.of('y', 'v', 10),
                                Edge.of('y', 'w', 1)));
        Algorithm<Event, FloydWarshall.Data<Character>> alg = new FloydWarshall<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();
        alg.run(ec, new FloydWarshall.Data<>(costs));

        ec.visitEvents(new LogEventVisitor());
    }
}
