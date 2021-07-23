package com.github.cbl.algorithm_analyzer.sorts.heapsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class HeapSort<T extends Comparable<T>> implements Algorithm<Event, HeapSort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record FinalStateEvent<T>(T[] arr, int from, int to, int limit) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            int[] colors = new int[arr.length];
            for (int i = 0; i < limit; i++) {
                colors[i] = 3;
            }
            for (int i = limit; i < arr.length; i++) {
                colors[i] = 4;
            }
            colors[from] = 1;
            colors[to] = 2;
            sj.add(ArrayPrinter.toString(arr, colors));
            return sj.toString();
        }
    }

    public static record PartialStateEvent<T>(
            T[] array, long comparisons, long writes, String message) implements Event {

        public PartialStateEvent(T[] array, long comparisons, long writes) {
            this(array, comparisons, writes, null);
        }

        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if (null != message) {
                sj.add(message);
                sj.add("=".repeat(message.length()));
            }
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
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();
        buildUpMaximumHeap(arr, c, w, events);

        for (int i = arr.length; i > 1; i--) {
            w.change(arr, 0, i - 1);
            descendMaxHeap(1, i - 1, arr, c, w, events);
            events.accept(
                    new PartialStateEvent<T>(
                            arr.clone(), c.getComparisonsSnapshot(), w.getWritesSnapshot()));
        }
        events.accept(new PartialStateEvent<T>(arr, c.getComparisons(), w.getWrites()));
    }
    ;

    /**
     * Descends element at {@code index} to reinforce heap-properties in arr
     *
     * @param index 1-based index of lement to descend
     * @param limit 1-based limit (length of heap in arr)
     * @param arr the array that is / contains the max-heap
     */
    private void descendMaxHeap(
            int index,
            int limit,
            T[] arr,
            Comparator c,
            ArrayWriter w,
            EventConsumer<Event> events) {
        var worth = arr[index - 1];
        var max = 0;
        var maxWorth = arr[0];
        var oldIndex = index;

        events.accept(new FinalStateEvent<T>(arr.clone(), index - 1, index - 1, limit));

        do {
            oldIndex = index;

            if ((2 * index) <= limit && c.compare(arr[2 * index - 1], worth) > 0) {
                max = 2 * index;
                maxWorth = arr[2 * index - 1];
            } else {
                max = index;
                maxWorth = worth;
            }

            if ((2 * index + 1) <= limit && c.compare(arr[2 * index], maxWorth) > 0) {
                max = 2 * index + 1;
                maxWorth = arr[2 * index];
            }

            if (max != index) {
                w.set(arr, index - 1, arr[max - 1]);
                index = max;

                T[] tmp = arr.clone();
                tmp[max - 1] = worth;
                events.accept(new FinalStateEvent<T>(tmp, oldIndex - 1, max - 1, limit));
            }

        } while (oldIndex != max);

        if (arr[max - 1] != worth) {
            w.set(arr, max - 1, worth);
        }
    }
    ;

    private void buildUpMaximumHeap(
            T[] arr, Comparator c, ArrayWriter w, EventConsumer<Event> events) {
        for (int i = arr.length / 2; i > 0; i--) {
            descendMaxHeap(i, arr.length, arr, c, w, events);
            events.accept(
                    new PartialStateEvent<T>(
                            arr.clone(), c.getComparisonsSnapshot(), w.getWritesSnapshot()));
        }
        events.accept(
                new PartialStateEvent<T>(
                        arr.clone(),
                        c.getComparisons(),
                        w.getWrites(),
                        "Total after building up heap:"));
    }
    ;
}
