package com.github.cbl.algorithm_analyzer;

// import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;

import java.util.Comparator;

// import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
// import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.trees.AvlTree.AVLTree;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

public class Main {
    public static void main(String[] args) throws Exception {
        // final Integer[] array = { 6, 5, 4, 3, 2, 1 };

        // final Algorithm<Event,BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
        // final EventConsumer<Event> ec = new GeneralEventConsumer();

        // a.run(ec, new BubbleSort.Data<>(array));

        // ec.visitEvents(new LogEventVisitor());

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
