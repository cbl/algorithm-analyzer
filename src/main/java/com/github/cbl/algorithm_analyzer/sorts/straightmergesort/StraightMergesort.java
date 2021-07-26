package com.github.cbl.algorithm_analyzer.sorts.straightmergesort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class StraightMergesort<T extends Comparable<T>> implements Algorithm<Event, StraightMergesort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record PartialStateEvent<T>(T[] arr, int left, int middle, int right, String text)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if (text != null) {
                sj.add(text);
            }
            int[] colors = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                colors[i] = -1;
            }
            if (left != middle || middle != right) {
                for (int i = left; i <= middle; i++) {
                    colors[i] = 1;
                }
                for (int i = middle + 1; i <= right; i++) {
                    colors[i] = 2;
                }
            }
            sj.add(ArrayPrinter.toString(arr, colors));
            return sj.toString();
        }
    }

    public static record FinalStateEvent<T>(
            T[] arr, int left, int middle, int right, long comparisons, long writes)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            sj.add("Array A after sorting and merging:");
            int[] colors = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                colors[i] = -1;
            }
            if (left != middle || middle != right) {
                for (int i = left; i <= middle; i++) {
                    colors[i] = 1;
                }
                for (int i = middle + 1; i <= right; i++) {
                    colors[i] = 2;
                }
            }
            sj.add(ArrayPrinter.toString(arr, colors));
            sj.add("Comparisons: " + comparisons);
            sj.add("Writes: " + writes);
            sj.add("\n\n\n");

            return sj.toString();
        }
    }

    @Override
    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();

        int sortlength = 1;
        int n = arr.length-1;

        while(sortlength < n){
            int right = -1;
            while(right + sortlength < n){
                int left = right + 1;
                int middle = left + sortlength - 1;
                if(middle + sortlength <= n){
                    right = middle + sortlength;
                }
                else{
                    right = n;
                }
                events.accept(new PartialStateEvent<T>(arr.clone(), left, middle, right, "Array A:"));
                merge(arr, left, middle, right, c, w, events);
                events.accept(new FinalStateEvent<T>(arr.clone(), left, right, right, c.getComparisonsSnapshot(), w.getWritesSnapshot()));
            }
            sortlength = 2 * sortlength;
        }
        events.accept(new FinalStateEvent<T>(arr, 0, 0, 0, c.getComparisons(), w.getWrites()));
    }
    ;

    public void merge(
        T[] arrA,
        int left,
        int middle,
        int right,
        Comparator c,
        ArrayWriter w,
        EventConsumer<Event> events) {
    T[] arrB = arrA.clone();

    for (int j = 0; j < arrB.length; j++) {
        arrB[j] = null;
    }

    for (int i = left; i <= middle; i++) {
        w.set(arrB, i, arrA[i]);
    }

    for (int j = middle + 1; j <= right; j++) {
        w.set(arrB, right + middle + 1 - j, arrA[j]);
    }

    events.accept(new PartialStateEvent<T>(arrB.clone(), 0, 0, 0, "Array B:"));

    int i = left;
    int j = right;
    int k = left;

    while (i < j) {
        if (c.compare(arrB[i], arrB[j]) <= 0) {
            w.set(arrA, k, arrB[i]);
            i++;
        } else {
            w.set(arrA, k, arrB[j]);
            j--;
        }
        k++;
    }
    w.set(arrA, right, arrB[i]);
}}

