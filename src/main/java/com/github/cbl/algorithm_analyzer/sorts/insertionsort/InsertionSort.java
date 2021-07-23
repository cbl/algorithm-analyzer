package com.github.cbl.algorithm_analyzer.sorts.insertionsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class InsertionSort<T extends Comparable<T>>
        implements Algorithm<Event, InsertionSort.Data<T>> {

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

    @Override
    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();

        T tmp = null;
        for (int i = 1; i < arr.length; i++) {
            tmp = arr[i];
            int k = i;
            while (k > 0 && c.compare(arr[k - 1], tmp) > 0) {
                w.write(arr, k, arr[k - 1]);
                k--;
            }
            w.write(arr, k, tmp);
            events.accept(
                    new PartialStateEvent<T>(
                            arr.clone(), c.getComparisonsSnapshot(), w.getWritesSnapshot()));
        }

        events.accept(new PartialStateEvent<T>(arr, c.getComparisons(), w.getWrites()));
    }
}
