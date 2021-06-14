package com.github.cbl.algorithm_analyzer;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;
import com.github.cbl.algorithm_analyzer.graphs.AdjacentMatrixGraph;
import com.github.cbl.algorithm_analyzer.graphs.deepsearch.Deepsearch;
import com.github.cbl.algorithm_analyzer.sorts.shellsort.Shellsort;
import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.trees.AvlTree.AVLTree;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws Exception {
        Main.shellSort();
    }

    public static void tiefenSuche() {
        int size = 4;
        String[] nodeNames = {"A", "B", "C", "D"};
        Graph<Integer, boolean[][]> graph = new AdjacentMatrixGraph(size);

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
        final Integer[] array = { 6, 5, 4, 3, 2, 1 };

        final Algorithm<Event,BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new BubbleSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }

    public static void shellSort() {
        final Integer[] array = { 6, 5, 4, 3, 2, 1 };

        final Algorithm<Event,Shellsort.Data<Integer>> a = new Shellsort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new Shellsort.Data<>(array));

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
}
