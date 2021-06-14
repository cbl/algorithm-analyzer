package com.github.cbl.algorithm_analyzer.sorts.shellsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Shellsort<T extends Comparable<T>> implements Algorithm<Event, Shellsort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record PartialStateEvent<T>(T[] array, long comparisons, long writes, long stepWidth)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add(ArrayPrinter.toString(array));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);
            sj.add("Stepwidth: " + stepWidth);

            return sj.toString();
        }
    }
    ;

    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();

        int stepSize = 1;

        while ((3 * stepSize + 1) < arr.length) {
            stepSize = 3 * stepSize + 1;
        }

        while (stepSize > 0) {
            for (int i = stepSize; i < arr.length; i++) {
                T current = arr[i];
                int k = i;
                for (; k >= stepSize; k -= stepSize) {
                    if (c.compare(arr[k - stepSize], current) <= 0) {
                        break;
                    }
                    w.set(arr, k, arr[k - stepSize]);
                }
                w.set(arr, k, current);

                events.accept(
                        new PartialStateEvent<T>(
                                arr.clone(), c.getComparisonsSnapshot(), w.getWritesSnapshot(), stepSize));
            }
            stepSize = stepSize / 3;
        }

        events.accept(new PartialStateEvent<T>(arr, c.getComparisons(), w.getWrites(), stepSize));
    }
    ;
}
