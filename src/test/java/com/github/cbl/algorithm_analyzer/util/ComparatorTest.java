package com.github.cbl.algorithm_analyzer.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComparatorTest {

    private static record C(int i) implements Comparable<C> {

        @Override
        public int compareTo(C o) {
            return o.i - i; // native order reversed
        }

    }

    @Test
    @DisplayName("Should compare int correctly")
    public void compareInts() {
        Comparator c = new Comparator();
        assertEquals(c.compare(0, 0), 0, "Compare equals ints");
        assertTrue(c.compare(0, 1) < 0, "Compare a < b ints");
        assertTrue(c.compare(1, 0) > 0, "Compare a > b ints");
    }

    @Test
    @DisplayName("Should obey custom Comparable instance")
    public void compareComparable() {
        Comparator c = new Comparator();
        assertEquals(c.compare(new C(0), new C(0)), 0, "Compare equals object");
        assertTrue(c.compare(new C(0), new C(1)) > 0, "Compare a < b object");
        assertTrue(c.compare(new C(1), new C(0)) < 0, "Compare a > b object");
    }

    @Test
    @DisplayName("Handles snapshots correctly")
    public void handlesSnapshotsCorrectly() {
        Comparator c = new Comparator();

        c.compare(0, 0);
        c.compare(0, 0);
        assertEquals(c.getComparisonsSnapshot(), 2, "Calculates first snapshot correctly");

        c.compare(0, 0);
        c.compare(0, 0);
        assertEquals(c.getComparisonsSnapshot(), 2, "Calculates later snapshots correctly");

        assertEquals(c.getComparisons(), 4, "Calculates overall correctly");
    }
    
}
