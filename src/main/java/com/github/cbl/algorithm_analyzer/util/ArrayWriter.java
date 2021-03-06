package com.github.cbl.algorithm_analyzer.util;

/** Utility class for changing array field and storing the number of writes */
public class ArrayWriter {

    private long writes = 0;
    private long snapshot = 0;

    public <T> void change(T[] arr, int i, int j) {
        assert (arr != null);
        assert (i >= 0 && i < arr.length);
        assert (j >= 0 && j < arr.length);

        if (i == j) {
            // TODO: ist das so richtig? Wie ist das genau definiert mit den 'Schreibzugriffen'?
            writes++;
        } else {
            final T tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            writes += 2;
        }
    }

    public <T> void write(T[] arr, int index, T el) {
        assert (arr != null);
        assert (index >= 0 && index < arr.length);

        arr[index] = el;
        writes++;
    }

    public <T> void set(T[] arr, int i, T value) {
        assert (arr != null);
        assert (i >= 0 && i < arr.length);

        arr[i] = value;
        writes++;
    }

    public long getWrites() {
        return writes;
    }

    public long getWritesSnapshot() {
        long oldSnapshot = snapshot;
        snapshot = writes;
        return writes - oldSnapshot;
    }
}
