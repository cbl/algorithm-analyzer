package com.github.cbl.algorithm_analyzer.sorts.countingsort;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Countingsort<T extends Comparable<T>> implements Algorithm<Event, Countingsort.Data<T>> {

    public static record Data<T extends Comparable<T>>(T[] array) {}

    public static record PartialStateEvent<T>(Integer[] array, Integer[] arrayC, Integer step, String text)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if(text!= null){
                sj.add(text);
            }
            if(step != null){
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

    @Override
    public void run(EventConsumer<Event> events, Data<T> data) {
        final T[] arr = data.array();
        final Comparator c = new Comparator();
        final ArrayWriter w = new ArrayWriter();

        int k = findHighestNumber(arr,c);

        countingsort(arr, k, w, events);
    }
    ;

    public int findHighestNumber(T[] arr, Comparator c){
        int pos = 0;
        for(int i = 1; i < arr.length; i++){
            if(c.compare(arr[i], arr[pos]) > 0){
                pos = i;
            }
        }
        return pos;
    }

    /**
     * Sorts elements by using their frequency
     *
     * @param arr the unsorted array 
     * @param k highest value of @param arr
     */
    public void countingsort(T[] arr, int k, ArrayWriter w, EventConsumer<Event> events){
        Integer[] arra = (Integer[])arr;
        Integer[] arrc = new Integer[arra[k]];
        Integer[] arrb = new Integer[arra.length];

        for(int i = 0; i < arra[k]; i++){
            arrc[i] = 0;
        }

        for(int i = 0; i < arrb.length; i++){
            arrb[i] = 0;
        }

        for(int j = 0; j < arra.length; j++){
            w.set(arrc, arra[j]-1, arrc[arra[j]-1]+1);
        }

        for(int i = 1; i < arra[k]; i++){            
            w.set(arrc, i, arrc[i] + arrc[i-1]);
        }
        events.accept(
                new PartialStateEvent<T>(
                        arrb.clone(), arrc.clone(), null, "Array after counting all elements:"));

        int step = 0;
        for(int j = arra.length-1; j >= 0; j--){
            step++;
            w.set(arrb, arrc[arra[j]-1]-1, arra[j]);
            w.set(arrc, arra[j]-1, (arrc[arra[j]-1])-1);
            
            events.accept(
                new PartialStateEvent<T>(
                        arrb.clone(), arrc.clone(), step, null));
        }

        events.accept(
                new PartialStateEvent<T>(
                        arrb.clone(), arrc.clone(), null, "Final result:"));

        for(int i = 0; i < arra.length; i++){
            w.set(arra, i, arrb[i]);
        }
    }
}
