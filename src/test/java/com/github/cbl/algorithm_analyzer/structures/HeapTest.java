package com.github.cbl.algorithm_analyzer.structures;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.util.ArrayWriter;
import com.github.cbl.algorithm_analyzer.util.Comparator;
import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;

import org.junit.jupiter.api.Test;

public class HeapTest {

    @Test
    public void testMaxHeap() {
        Integer[] arr = {2, 15, 7, 12, 13, 20, 38, 1};
        final var c = Comparator.<Integer>naturalOrder();
        final var w = new ArrayWriter();
        final EventConsumer<Event> ec = new GeneralEventConsumer();
        Heap.heapify(arr, arr.length, c, w, ec);

        Integer[] expected = { 38, 15, 20, 12, 13, 2, 7, 1 };
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testMinHeap() {
        Integer[] arr = {2, 15, 7, 12, 13, 20, 38, 1};
        final var c = Comparator.<Integer>naturalOrder().reversed();
        final var w = new ArrayWriter();
        final EventConsumer<Event> ec = new GeneralEventConsumer();
        Heap.heapify(arr, arr.length, c, w, ec);

        Integer[] expected = { 1, 2, 7, 12, 13, 20, 38, 15 };
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testIsMaxHeap() {
        Integer[] maxHeap = { 38, 15, 20, 12, 13, 2, 7, 1 };
        assertEquals(true, Heap.isMaxHeap(maxHeap));
    }

    @Test
    public void testIsMinHeap() {
        Integer[] minHeap = { 1, 2, 7, 12, 13, 20, 38, 15 };
        assertEquals(true, Heap.isMinHeap(minHeap));
    }
    
}
