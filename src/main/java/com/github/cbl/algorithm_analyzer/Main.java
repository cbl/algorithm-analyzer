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
import com.github.cbl.algorithm_analyzer.graphs.floydwarshall.FloydWarshall;
import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.sorts.heapsort.HeapSort;
import com.github.cbl.algorithm_analyzer.sorts.quicksort.Quicksort;
import com.github.cbl.algorithm_analyzer.sorts.shellsort.Shellsort;
import com.github.cbl.algorithm_analyzer.trees.AvlTree.AVLTree;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

import java.util.Comparator;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        Main.heapSort();
    }

    public static void tiefenSuche() {
        int size = 4;
        String[] nodeNames = {"A", "B", "C", "D"};
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
