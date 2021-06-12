package com.github.cbl.algorithm_analyzer;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;
import com.github.cbl.algorithm_analyzer.util.LogEventVisitor;

public class Main {
    public static void main(String[] args) throws Exception {
        final Integer[] array = { 6, 5, 4, 3, 2, 1 };

        final Algorithm<Event,BubbleSort.Data<Integer>> a = new BubbleSort<Integer>();
        final EventConsumer<Event> ec = new GeneralEventConsumer();

        a.run(ec, new BubbleSort.Data<>(array));

        ec.visitEvents(new LogEventVisitor());
    }
}