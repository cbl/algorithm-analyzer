package com.github.cbl.algorithm_analyzer.sorts.quicksort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Quicksort<T extends Comparable<T>> implements Algorithm<Event, Quicksort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record FinalStateEvent<T>(T[] array, long comparisons, long writes)
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

    public static record PartialStateEvent<T>(T[] array, Range range, long comparisons, long writes)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add(ArrayPrinter.toString(array));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);
            sj.add("Partition range: [" + (range.p + 1) + "-" + (range.q + 1) + "]");

            return sj.toString();
        }
    }

    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator<T> c = Comparator.naturalOrder();
        final ArrayWriter w = new ArrayWriter();

        quicksort(arr, 0, arr.length - 1, c, w, events);

        events.accept(new FinalStateEvent<T>(arr.clone(), c.getComparisons(), w.getWrites()));
    }

    record Range(int p, int q) {}

    private void quicksort(
            T[] arr, int i, int j, Comparator<T> c, ArrayWriter w, EventConsumer<Event> events) {
        if (i < j) {
            var range = partition(arr, i, j, c, w, events);

            events.accept(
                    new PartialStateEvent<>(
                            arr.clone(),
                            new Range(i, j),
                            c.getComparisonsSnapshot(),
                            w.getWritesSnapshot()));

            quicksort(arr, i, range.p, c, w, events);
            quicksort(arr, range.q, j, c, w, events);
        }
    }

    private Range partition(
            T[] arr, int i, int j, Comparator<T> c, ArrayWriter w, EventConsumer<Event> events) {
        var p = arr[j];

        while (i <= j) {
            while (c.compare(arr[i], p) < 0) {
                i++;
            }
            while (c.compare(arr[j], p) > 0) {
                j--;
            }
            if (i <= j) {
                w.change(arr, i, j);
                i++;
                j--;
            }
        }
        return new Range(j, i);
    }
}
