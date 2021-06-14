package com.github.cbl.algorithm_analyzer.sorts.bubblesort;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.github.cbl.algorithm_analyzer.util.GeneralEventConsumer;

import org.junit.jupiter.api.Test;

public class BubbleSortTest {

    @Test
    public void testBubbleSort() {
        BubbleSort<Integer> qs = new BubbleSort<>();
        final var expected = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8};

        var arr1 = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8};
        qs.run(new GeneralEventConsumer(), new BubbleSort.Data<Integer>(arr1));
        assertArrayEquals(expected, arr1);

        var arr2 = new Integer[] {8, 7, 6, 5, 4, 3, 2, 1};
        qs.run(new GeneralEventConsumer(), new BubbleSort.Data<Integer>(arr2));
        assertArrayEquals(expected, arr2);

        var arr3 = new Integer[] {7, 2, 8, 1, 6, 4, 5, 3};
        qs.run(new GeneralEventConsumer(), new BubbleSort.Data<Integer>(arr3));
        assertArrayEquals(expected, arr3);
    }
}
