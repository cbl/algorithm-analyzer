package com.github.cbl.algorithm_analyzer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArrayWriterTest {

    @Test
    @DisplayName("Should throw if array is null")
    public void throwOnNullArray() {
        ArrayWriter w = new ArrayWriter();
        assertThrows(Error.class, () -> {
            w.change(null, 0, 0);
        }, "Throws if array is null");
    }

    @Test
    @DisplayName("Should throw on bad indices")
    public void throwOnBadIndices() {
        ArrayWriter w = new ArrayWriter();
        Integer[] arr = {0, 1};

        assertThrows(Error.class, () -> {
            w.change(arr, 2, 0);
        }, "Throws if first index is out of bounds");

        assertThrows(Error.class, () -> {
            w.change(arr, -1, 0);
        }, "Throws if first index is out of bounds");

        assertThrows(Error.class, () -> {
            w.change(arr, 0, 2);
        }, "Throws if second index is out of bounds");

        assertThrows(Error.class, () -> {
            w.change(arr, 0, -1);
        }, "Throws if second index is out of bounds");
    }


    @Test
    @DisplayName("Calculaes writes correctly")
    public void calculatesWritesCorrectly() {
        ArrayWriter w = new ArrayWriter();
        Integer[] arr = { 0, 1, 2, 3 };

        assertEquals(w.getWrites(), 0);

        w.change(arr, 0, 1);

        assertEquals(w.getWrites(), 2);

        w = new ArrayWriter();

        w.change(arr, 0, 0);

        assertEquals(w.getWrites(), 1);
    }

    @Test
    @DisplayName("Handles snapshots correctly")
    public void handlesSnapshotsCorrectly() {
        ArrayWriter w = new ArrayWriter();
        Integer[] arr = { 0, 1, 2, 3 };

        w.change(arr, 0, 1);
        assertEquals(w.getWritesSnapshot(), 2, "Calculates first snapshot correctly");

        w.change(arr, 0, 1);
        assertEquals(w.getWritesSnapshot(), 2, "Calculates first snapshot correctly");

        assertEquals(w.getWrites(), 4, "Calculates overall correctly");
    }
}
