package com.github.cbl.algorithm_analyzer.search;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;

import java.util.Arrays;
import java.util.StringJoiner;

public class BinarySearch<T extends Comparable<T>> implements Algorithm<Event, BinarySearch.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array, T searchedValue) {}

    public static record FinalStateEvent<T>(T[] array, int left, int right, int middle) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            int[] colors = new int[array.length];
            Arrays.fill(colors, -1);

            if (left <= right) { // search was successful
                colors[middle] = 1; // found index -> green
                sj.add(ArrayPrinter.toString(array, colors));
            } else {
                sj.add("Element was not found!");
            }

            return sj.toString();
        }
    }

    public static record PartialStateEvent<T>(T[] array, int left, int right, int middle)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            int[] colors = new int[array.length];
            Arrays.fill(colors, -1);

            // left and right bounds -> yellow
            colors[left] = 2;
            colors[right] = 2;

            if (left == middle && right == middle) {
                colors[middle] = 0; // middle -> red
            } else {
                colors[middle] = 4; // middle -> purple
            }

            sj.add(ArrayPrinter.toString(array, colors));

            return sj.toString();
        }
    }

    public void run(EventConsumer<Event> events, Data<T> data) {
        Arrays.sort(data.array()); // make sure the input array is sorted

        binarySearch(events, data.array(), data.searchedValue());
    }

    private boolean binarySearch(EventConsumer<Event> events, T[] arr, T searchedValue) {
        int left = 0;
        int right = arr.length - 1;
        int middle = 0;

        while (left <= right) {
            middle = (int) Math.floor((left + right) / 2.0);

            if (arr[middle].equals(searchedValue)) {
                events.accept(new FinalStateEvent<T>(arr, left, right, middle));

                return true;
            }

            events.accept(new PartialStateEvent<T>(arr, left, right, middle));

            if (arr[middle].compareTo(searchedValue) > 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        events.accept(new FinalStateEvent<T>(arr, left, right, middle));

        return false;
    }
}
