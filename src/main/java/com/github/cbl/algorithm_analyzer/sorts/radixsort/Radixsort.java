package com.github.cbl.algorithm_analyzer.sorts.radixsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Radixsort<T extends Comparable<T>>
        implements Algorithm<Event, Radixsort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array, int amount) {}

    public static record PartialStateEvent<T>(
            Integer[] array, Integer[] arrayC, Integer step, String text) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if (text != null) {
                sj.add(text);
            }
            if (step != null) {
                sj.add("Step: " + step);
            }
            sj.add("Array C:");
            sj.add(ArrayPrinter.toString(arrayC));
            sj.add("Array B:");
            sj.add(ArrayPrinter.toString(array));

            return sj.toString();
        }
    }
    ;

    public static record FinalStateEvent<T>(
            T[] array,  Integer number) implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if(number > 0) sj.add("Array A nach pruefen der Stelle: " + number);
            else sj.add("Finales Ergebnis: ");
            sj.add(ArrayPrinter.toString(array));
            sj.add("\n\n");
            return sj.toString();
        }
    }
    ;

    @Override
    public void run(EventConsumer<Event> events, Data<T> data) {
        final int d = data.amount();
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();

        for(int k = d-1; k >= 0; k--){
            countingsort(arr, d, k, w, events);

            events.accept(
            new FinalStateEvent<T>(
                    arr.clone(), k+1));
        }
        events.accept(
            new FinalStateEvent<T>(
                    arr, 0));
    }
    ;

    public void countingsort(T[] arr, int d, int k,  ArrayWriter w, EventConsumer<Event> events) {
        Integer[] arra = (Integer[]) arr;
        Integer[] arrc = new Integer[d];
        Integer[] arrb = new Integer[arra.length];

        for (int i = 0; i < d; i++) {
            arrc[i] = 0;
        }

        for (int i = 0; i < arrb.length; i++) {
            arrb[i] = null;
        }

        for (int j = 0; j < arra.length; j++) {
            String number1 = Integer.toString(arra[j]);
            char char1 = number1.charAt(k);
            w.set(arrc, char1-49, (arrc[char1-49]) + 1);
        }

        for (int i = 1; i < d; i++) {
            w.set(arrc, i, arrc[i] + arrc[i - 1]);
        }
        events.accept(
                new PartialStateEvent<T>(
                        arrb.clone(), arrc.clone(), null, "Array nach Auszahelen der Hauefigkeit:"));

        int step = 0;
        for (int j = arra.length - 1; j >= 0; j--) {
            step++;

            String number1 = Integer.toString(arra[j]);
            char char1 = number1.charAt(k);

            w.set(arrb, arrc[char1-49]-1, arra[j]);
            w.set(arrc, char1-49, (arrc[char1-49]) - 1);

            events.accept(new PartialStateEvent<T>(arrb.clone(), arrc.clone(), step, null));
        }

        for (int i = 0; i < arra.length; i++) {
            w.set(arra, i, arrb[i]);
        }
    }
}
