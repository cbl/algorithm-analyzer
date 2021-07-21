package com.github.cbl.algorithm_analyzer.structures;

import java.util.StringJoiner;
import java.util.function.Consumer;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

public interface Heap {

    record SinkEvent<T>(T[] arr, int from, int to) implements Event {
        @Override
        public String toString() {
            int[] colors = new int[arr.length];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = -1;
            }
            colors[from] = 1;
            colors[to] = 2;
            return ArrayPrinter.toString(arr, colors);
        }
    }

    record RiseEvent<T>(T[] arr, int from, int to) implements Event {
        @Override
        public String toString() {
            int[] colors = new int[arr.length];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = -1;
            }
            colors[from] = 1;
            colors[to] = 2;
            return ArrayPrinter.toString(arr, colors);
        }
    }

    record SinkRunEvent<T>(T[] arr, long comparisons, long writes) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);

            return sj.toString();
        }
    }

    record RiseRunEvent<T>(T[] arr, long comparisons, long writes) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);

            return sj.toString();
        }
    }


    record HeapifyEvent<T>(T[] arr, long comparisons, long writes) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add(ArrayPrinter.toString(arr));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);

            return sj.toString();
        }
    }


    /**
     * Builds max heap, if comparator compares by natural order
     * @param <T> the type of heap elements
     * @param arr the array to heapify
     * @param size the size of the (partial) array to heapify
     * @param cmp the comparator to use
     * @param w the writer to use
     */
    static <T extends Comparable<T>> void heapify(T[] arr, int size, Comparator<T> cmp, ArrayWriter w, Consumer<Event> events) {
        for (int i = (size - 1) / 2; i >= 0; i--) {
            sink(arr, i, size, cmp, w, events);
            events.accept(new SinkRunEvent<T>(arr.clone(), cmp.getComparisonsSnapshot(), w.getWritesSnapshot()));
        }

        events.accept(new HeapifyEvent<>(arr.clone(), cmp.getComparisons(), w.getWrites()));
    }

    static <T extends Comparable<T>> void decreaseKey(T[] arr, int index, T newKey, Comparator<T> cmp, ArrayWriter w, Consumer<Event> events) {
        arr[index] = newKey;
        rise(arr, index, cmp, w, events);
        events.accept(new RiseRunEvent<T>(arr.clone(), cmp.getComparisonsSnapshot(), w.getWritesSnapshot()));
    }

    /**
     * Sinks an element inside a heap
     * 
     * @param <T> the type of heap elements
     * @param arr the heap
     * @param index the index of the element to sink
     * @param size the effective size of the heap
     * @param cmp the comparator to use - natural order comparison relates to max-heap
     * @param w the writer to use
     */
    static <T extends Comparable<T>> void sink(T[] arr, int index, int size, Comparator<T> cmp, ArrayWriter w, Consumer<Event> events) {
        T el = arr[index];
        int oldIndex = index;
        int max = index;

        do {
            oldIndex = index;
            int c1 = 2 * index + 1;
            int c2 = 2 * index + 2;

            if (c1 < size && cmp.compare(arr[c1], el) > 0) {
                max = c1;
            } else {
                max = index;
            }

            if (c2 < size && cmp.compare(arr[c2], arr[max]) > 0) {
                max = c2;
            }

            if (max != index) {
                //FIXME: replace with w.write(…) after insertionsort PR is merged!
                arr[index] = arr[max];

                {
                    T[] tmp = arr.clone();
                    tmp[max] = el;
                    events.accept(new SinkEvent<T>(tmp, max, index));
                }

                index = max;
            } else {
                T[] tmp = arr.clone();
                tmp[max] = el;
                events.accept(new SinkEvent<T>(tmp, index, index));
            }
        } while (oldIndex != max);

        //FIXME: replace with w.write(…) after insertionsort PR is merged!
        arr[max] = el;
    }


    static <T extends Comparable<T>> void rise(T[] arr, int index, Comparator<T> cmp, ArrayWriter w, Consumer<Event> events) {
        while (index > 0) {
            int p = (index - 1) / 2;
            if (cmp.compare(arr[index], arr[p]) > 0) {
                w.change(arr, index, p);
                events.accept(new RiseEvent<T>(arr.clone(), index, p));
            }
            index = p;
        }
    }

    static <T extends Comparable<T>> boolean isMaxHeap(T[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int c1 = i * 2 + 1;
            int c2 = i * 2 + 2;
            if (c1 < arr.length && arr[c1].compareTo(arr[i]) > 0) {
                return false;
            }
            if (c2 < arr.length && arr[c2].compareTo(arr[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    static <T extends Comparable<T>> boolean isMinHeap(T[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int c1 = i * 2 + 1;
            int c2 = i * 2 + 2;
            if (c1 < arr.length && arr[c1].compareTo(arr[i]) < 0) {
                return false;
            }
            if (c2 < arr.length && arr[c2].compareTo(arr[i]) < 0) {
                return false;
            }
        }
        return true;
    }
}
