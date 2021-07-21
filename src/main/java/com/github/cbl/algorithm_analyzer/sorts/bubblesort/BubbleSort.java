package com.github.cbl.algorithm_analyzer.sorts.bubblesort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class BubbleSort<T extends Comparable<T>> implements Algorithm<Event, BubbleSort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record PartialStateEvent<T>(T[] array, long comparisons, long writes)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add(ArrayPrinter.toString(array));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);

            return sj.toString();
        }
    }
    ;

    @Override
    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator<T> c = Comparator.naturalOrder();
        final ArrayWriter w = new ArrayWriter();

        boolean wasSorted = true;
        do {
            wasSorted = true;
            for (int i = 0; i < arr.length - 1; i++) {
                if (c.compare(arr[i], arr[i + 1]) > 0) {
                    wasSorted = false;
                    w.change(arr, i, i + 1);
                }
            }
            events.accept(
                    new PartialStateEvent<T>(
                            arr.clone(), c.getComparisonsSnapshot(), w.getWritesSnapshot()));
        } while (!wasSorted);

        events.accept(new PartialStateEvent<T>(arr, c.getComparisons(), w.getWrites()));
    }
    ;
}
