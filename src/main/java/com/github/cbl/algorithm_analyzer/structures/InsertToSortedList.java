package com.github.cbl.algorithm_analyzer.structures;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

public class InsertToSortedList<T extends Comparable<T>> implements Algorithm<Event, InsertToSortedList.Data<T>> {

    public static record Data<T extends Comparable<T>>(List<T> array, T insertValue) {}

    public static record FinalStateEvent<T>(
            List<T> array, T insertValue, int left, int right, int middle) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");

            int[] colors = new int[array.size()];
            Arrays.fill(colors, -1);
            colors[left] = 1;

            sj.add(ArrayPrinter.toString(array.toArray(), colors));
            sj.add(String.format("Inserted %s at index %d", insertValue, (middle + 1)));
            return sj.toString();
        }
    }

    public void run(EventConsumer<Event> events, Data<T> data) {
        insert(events, data.array(), data.insertValue());
    }

    private void insert(EventConsumer<Event> events, List<T> arr, T insertValue) {
        int left = 0;
        int right = arr.size() - 1;
        int middle = 0;

        while (left <= right) {
            middle = (left + right) / 2;

            if (arr.get(middle).equals(insertValue)) {
                return;
            }

            if (arr.get(middle).compareTo(insertValue) > 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        for(int i=right;i>=left;i--) {
            arr.set(i+1, arr.get(i));
        }

        arr.add(left, insertValue);

        events.accept(new FinalStateEvent<T>(new ArrayList<T>(arr), insertValue, left, right, middle));
    }
}
