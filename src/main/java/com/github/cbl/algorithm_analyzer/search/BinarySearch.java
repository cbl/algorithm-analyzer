package com.github.cbl.algorithm_analyzer.search;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;

import java.util.Arrays;
import java.util.StringJoiner;

public class BinarySearch<T extends Comparable<T>>
        implements Algorithm<Event, BinarySearch.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array, T searchedValue) {}

    public static record FinalStateEvent<T>(
            T[] array, T searchedValue, int left, int right, int middle) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            int[] colors = new int[array.length];
            Arrays.fill(colors, -1);

            colors[left] = 2; // left index -> yellow
            colors[right] = 3; // right index -> orange

            if (left <= right) { // search was successful
                colors[middle] = 1; // found index -> green

                sj.add(ArrayPrinter.toString(array, colors));
                sj.add(
                        String.format(
                                "Element %s was found at index %d", searchedValue, (middle + 1)));
            } else {
                sj.add(ArrayPrinter.toString(array, colors));
                sj.add(String.format("Element %s was not found!", searchedValue));
            }

            return sj.toString();
        }
    }

    public static record PartialStateEvent<T extends Comparable<T>>(
            T[] array, T searchedValue, int left, int right, int middle) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            int[] colors = new int[array.length];
            Arrays.fill(colors, -1);

            colors[left] = 2; // left index -> yellow
            colors[right] = 3; // right index -> orange

            colors[middle] = 4; // middle index -> purple

            sj.add(ArrayPrinter.toString(array, colors));

            // print the next values of left or right:
            if (array[middle].compareTo(searchedValue) > 0) {
                sj.add(
                        String.format(
                                "%s is smaller than %s, new right index will be %d",
                                searchedValue, array[middle], middle));
            } else {
                sj.add(
                        String.format(
                                "%s is greater than %s, new left index will be %d",
                                searchedValue, array[middle], middle + 2));
            }

            return sj.toString();
        }
    }

    public void run(EventConsumer<Event> events, Data<T> data) {
        Arrays.sort(data.array()); // make sure the input array is sorted

        binarySearch(events, data.array(), data.searchedValue());
    }

    private void binarySearch(EventConsumer<Event> events, T[] arr, T searchedValue) {
        int left = 0;
        int right = arr.length - 1;
        int middle = 0;

        while (left <= right) {
            middle = (left + right) / 2;

            if (arr[middle].equals(searchedValue)) {
                events.accept(new FinalStateEvent<T>(arr, searchedValue, left, right, middle));

                return;
            }

            events.accept(new PartialStateEvent<T>(arr, searchedValue, left, right, middle));

            if (arr[middle].compareTo(searchedValue) > 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        events.accept(new FinalStateEvent<T>(arr, searchedValue, left, right, middle));
    }
}
