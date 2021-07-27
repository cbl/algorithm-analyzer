package com.github.cbl.algorithm_analyzer.directAccess.interpolation;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayPrinter;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;

import java.util.StringJoiner;

public class Interpolation implements Algorithm<Event,  Interpolation.Data> {

    public static record Data (Integer[] array, Integer searched) {}

    public static record PartialStateEvent(Integer[] array, long comparisons, int left, int right, int estimate, String text)
            implements Event {
        @Override
        public String toString() {
            final StringJoiner sj = new StringJoiner("\n");
            if (text != null) sj.add(text);
            int [] color = new int[array.length];
            for(int i = 0; i < color.length; i++){
                color[i] = -1;
            }
            if(left!= estimate && right != estimate){
                color[left] = 2;
                color[right] = 1;
                color[estimate] = 0;
            }
            else{
                if(left == estimate){
                    color[left] = 3;
                    color[right] = 1;
                    color[estimate] = 3;
                }

                else if(right == estimate){
                    color[left] = 2;
                    color[right] = 3;
                    color[estimate] = 3;
                }
            }
            sj.add(ArrayPrinter.toString(array, color));
            sj.add("Comparisons: " + comparisons);

            return sj.toString();
        }
    }
    ;

    public void run(EventConsumer<Event> events, Data data) {
        final Integer searched = data.searched();
        final Integer[] arr = data.array();
        final Comparator c = new Comparator();

        int left = 0;
        int right = arr.length-1;
        int estimate = 0;
        boolean check = false;

        System.out.println("\nYellow = left, red = estimated value, green = right. If the element is orange either left and estimated index are the same OR right and estimated index are the same.\n\n");

        while(left <= right){
            if(c.compare(arr[right], arr[left]) > 0){
                int second = ( arr[right] - arr[left]);
                int first = ( searched -  arr[left]);
                estimate = (int)(left + ((double)((double)first/(double)second))*(right-left)+(1/2));
            }
            else estimate = left;

            if(estimate < left || estimate > right) break;
            else{
                if(c.compare(arr[estimate], searched) == 0) {
                    check = true;
                    events.accept(
                    new PartialStateEvent(
                    arr.clone(), c.getComparisonsSnapshot(), left, right, estimate, "Element was found at index " + (estimate )));
                    break;
                }
                else if(c.compare(arr[estimate], searched) > 0){
                    right = estimate -1;
                    events.accept(
                    new PartialStateEvent(
                    arr.clone(), c.getComparisonsSnapshot(), left, (right - (estimate +1)), estimate, "Element is smaller than the estimated value: " + arr[estimate] + ". The new right index is: " + right));
                } 
                else if(c.compare(arr[estimate], searched) < 0){ 
                    left = estimate +1;
                    events.accept(
                    new PartialStateEvent(
                    arr.clone(), c.getComparisonsSnapshot(), (left-(estimate+1)), right, estimate, "Element is bigger than the estimated value: " + arr[estimate] + ". The new left index is: " + left));
                }
            }
        }

        if(!check){
            events.accept(
                new PartialStateEvent(
                arr, c.getComparisonsSnapshot(), estimate, estimate, estimate, "Element not found"));
        }

        events.accept(
            new PartialStateEvent(
            arr, c.getComparisons(), estimate, estimate, estimate, "Summary: "));
    }
    ;
}
