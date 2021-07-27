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
import com.github.cbl.algorithm_analyzer.hashing.BrentHashTable;
import com.github.cbl.algorithm_analyzer.hashing.ClosedHashTable;
import com.github.cbl.algorithm_analyzer.hashing.CoalescedHashTable;
import com.github.cbl.algorithm_analyzer.hashing.DoubleHashTable;
import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.sorts.countingsort.Countingsort;
import com.github.cbl.algorithm_analyzer.sorts.heapsort.HeapSort;
import com.github.cbl.algorithm_analyzer.sorts.insertionsort.InsertionSort;
import com.github.cbl.algorithm_analyzer.sorts.mergesort.Mergesort;
import com.github.cbl.algorithm_analyzer.sorts.quicksort.Quicksort;
import com.github.cbl.algorithm_analyzer.sorts.radixsort.Radixsort;
import com.github.cbl.algorithm_analyzer.sorts.selectionsort.Selectionsort;
import com.github.cbl.algorithm_analyzer.sorts.shellsort.Shellsort;
import com.github.cbl.algorithm_analyzer.sorts.straightmergesort.StraightMergesort;
import com.github.cbl.algorithm_analyzer.structures.BinarySearch;
import com.github.cbl.algorithm_analyzer.structures.InsertToSortedList;
import com.github.cbl.algorithm_analyzer.structures.Interpolation;
import com.github.cbl.algorithm_analyzer.trees.AvlTree.AVLTree;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        Main.radixSort();
    }

    public static void sortedList() {
        final Integer[] insertValues = {21, 25, 67, 32, 26, 50, 78, 40, 52, 66};
        final List<Integer> array = new ArrayList<>();

        final Algorithm<Event, InsertToSortedList.Data<Integer>> a = new InsertToSortedList<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        for (Integer value : insertValues) {
            a.run(ec, new InsertToSortedList.Data<Integer>(array, value));
        }

        ec.visitEvents(new LogEventVisitor());
    }

    public static void closedHashTable() {
        int size = 11;
        EventConsumer<Event> ec = new GeneralEventConsumer();
        ClosedHashTable.Hashing hashing =
                (int key, int p) -> {
                    return key % p;
                };
        ClosedHashTable.Probing probing =
                (int key, int j, int p) -> {
                    return (key % p) + j;
                };
        ClosedHashTable table = new ClosedHashTable(ec, size, hashing, probing);
        table.resizeFactor = 3 / 2;
        table.resizeAtOccupation = 0.9;

        table.insert(38);
        table.insert(45);
        table.insert(16);
        table.insert(60);
        table.insert(12);
        table.insert(78);

        ec.visitEvents(new LogEventVisitor());
    }

    public static void doubleHashTable() {
        int size = 11;
        EventConsumer<Event> ec = new GeneralEventConsumer();
        DoubleHashTable.Hashing hashing =
                (int key) -> {
                    return key % 11;
                };
        DoubleHashTable.Hashing doubleHashing =
                (int key) -> {
                    return (1 + (key % (11 - 1)));
                };
        DoubleHashTable table = new DoubleHashTable(ec, size, hashing, doubleHashing);

        table.insert(29);
        table.insert(12);
        table.insert(7);
        table.insert(19);
        table.insert(30);
        table.insert(40);
        table.insert(11);

        ec.visitEvents(new LogEventVisitor());
    }

    public static void brentHashTable() {
        int size = 11;
        EventConsumer<Event> ec = new GeneralEventConsumer();
        BrentHashTable.Hashing hashing =
                (int key) -> {
                    return key % 11;
                };
        BrentHashTable.Hashing doubleHashing =
                (int key) -> {
                    return (1 + (key % (9)));
                };
        BrentHashTable table = new BrentHashTable(ec, size, hashing, doubleHashing);

        table.insert(38);
        table.insert(45);
        table.insert(16);
        table.insert(60);
        table.insert(12);
        table.insert(78);

        ec.visitEvents(new LogEventVisitor());
    }

    public static void binarySearch() {
        final Integer[] array = {21, 25, 32, 33, 26, 40, 52, 53, 57, 60, 65, 66, 67, 78};
        final Integer searchedValue = 60;

        final Algorithm<Event, BinarySearch.Data<Integer>> a = new BinarySearch<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new BinarySearch.Data<Integer>(array, searchedValue));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void coalescedHashTable() {
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
                        Edge.of(0, 1, 4),
                        Edge.of(0, 2, 6),
                        Edge.of(0, 3, 7),
                        Edge.of(0, 4, 12),
                        Edge.of(1, 2, 5),
                        Edge.of(1, 3, 4),
                        Edge.of(1, 4, 8),
                        Edge.of(2, 3, 4),
                        Edge.of(2, 4, 11),
                        Edge.of(3, 4, 8));
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
                                Edge.of('A', 'B', 2),
                                Edge.of('A', 'C', 9),
                                Edge.of('A', 'D', 8),
                                Edge.of('B', 'C', 6),
                                Edge.of('B', 'D', 5),
                                Edge.of('B', 'E', 3),
                                Edge.of('C', 'B', 1),
                                Edge.of('C', 'E', 1),
                                Edge.of('D', 'C', 1),
                                Edge.of('E', 'D', 1),
                                Edge.of('E', 'C', 4)));

        final Algorithm<Event, Dijkstra.Data<Character>> a = new Dijkstra<>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Dijkstra.Data<Character>(costs, 'A'));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void tiefenSuche() {
        int size = 4;
        String[] nodeNames = {"1", "2", "3", "4"};
        WeightFreeGraph<Integer> graph = new AdjacentMatrixGraph(size);

        graph.setEdge(0, 1);
        graph.setEdge(0, 3);
        graph.setEdge(1, 0);
        graph.setEdge(1, 3);
        graph.setEdge(1, 2);
        graph.setEdge(2, 0);
        graph.setEdge(3, 2);

        final Algorithm<Event, Deepsearch.Data> a = new Deepsearch();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Deepsearch.Data(graph, nodeNames));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void insertionSort() {
        final Integer[] array = {15, 48, 22, 34, 27, 35, 14};

        final Algorithm<Event, InsertionSort.Data<Integer>> a = new InsertionSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new InsertionSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void bubbleSort() {
        final Integer[] array = {15, 48, 22, 34, 27, 35, 14};

        final Algorithm<Event, BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new BubbleSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void countingSort() {
        final Integer[] array = {2, 4, 2, 1, 1, 4, 2, 1, 4, 2};

        final Algorithm<Event, Countingsort.Data<Integer>> a = new Countingsort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Countingsort.Data<>(array));

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
        final Integer[] array = {8, 7, 4, 3, 6, 5, 2};

        final Algorithm<Event, Quicksort.Data<Integer>> a = new Quicksort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Quicksort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void mergeSort() {
        final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

        final Algorithm<Event, Mergesort.Data<Integer>> a = new Mergesort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Mergesort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void heapSort() {
        final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

        final Algorithm<Event, HeapSort.Data<Integer>> a = new HeapSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new HeapSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void straightMergesort() {
        final Integer[] array = {20, 54, 28, 31, 5, 24, 39, 14, 1, 15};

        final Algorithm<Event, StraightMergesort.Data<Integer>> a =
                new StraightMergesort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new StraightMergesort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void avlTree() {
        AVLTree<Integer> t = new AVLTree<Integer>(Comparator.naturalOrder());
        EventConsumer<Event> ec = new GeneralEventConsumer();
        t.onEvent(ec);

        t.insert(3);
        t.insert(1);
        t.insert(8);
        t.insert(2);
        t.insert(6);
        t.insert(9);
        t.insert(7);
        t.remove(1);
        t.insert(10);
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

    public static void selectionSort() {
        final Integer[] array = {64, 25, 12, 22, 11};

        final Algorithm<Event, Selectionsort.Data<Integer>> a = new Selectionsort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Selectionsort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void interpolation() {
        Integer search = 9;
        final Integer[] array = {1, 9, 13, 16, 18, 19, 20, 22, 23};

        final Algorithm<Event, Interpolation.Data> a = new Interpolation();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Interpolation.Data(array, search));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void radixSort() {
        int amountNumbers = 3;
        final Integer[] array = {313, 322, 113, 223, 213, 132};

        final Algorithm<Event, Radixsort.Data<Integer>> a = new Radixsort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Radixsort.Data<>(array, amountNumbers));

        ec.visitEvents(new LogEventVisitor());
    }
}
