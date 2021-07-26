package com.github.cbl.algorithm_analyzer.sorts.selectionsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Selectionsort<T extends Comparable<T>>
        implements Algorithm<Event, Selectionsort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record FinalStateEvent<T>(
            T[] array, long comparisons, long writes, int pos, int i) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            int[] colors = new int[array.length];
            for (int j = 0; j < colors.length; j++) {
                colors[j] = -1;
            }
            if (pos >= 0 && i >= 0) {
                colors[i] = 1;
                colors[pos] = 2;
            }
            sj.add(ArrayPrinter.toString(array, colors));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);

            return sj.toString();
        }
    }
    ;

    public static record PartialStateEvent<T>(T[] array, int pos, int i, String text)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add(text);
            int[] colors = new int[array.length];
            for (int j = 0; j < colors.length; j++) {
                colors[j] = -1;
            }
            if (pos >= 0 && i >= 0) {
                colors[i] = 1;
                colors[pos] = 2;
            }
            sj.add(ArrayPrinter.toString(array, colors));

            return sj.toString();
        }
    }
    ;

    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();
        int counter = 1;

        for (int i = arr.length - 1; i > 0; i--) {
            int pos = i;
            for (int j = 0; j < i; j++) {
                if (c.compare(arr[j], arr[pos]) > 0) pos = j;
            }
            if (pos != i) {
                events.accept(new PartialStateEvent<T>(arr.clone(), pos, i, "Step " + counter));
                counter++;

                w.change(arr, pos, i);
                events.accept(
                        new FinalStateEvent<T>(
                                arr.clone(),
                                c.getComparisonsSnapshot(),
                                w.getWritesSnapshot(),
                                pos,
                                i));
            }
        }

        events.accept(new FinalStateEvent<T>(arr, c.getComparisons(), w.getWrites(), -1, -1));
    }
    ;
}
