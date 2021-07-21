package com.github.cbl.algorithm_analyzer.util;

/** Utility class for comparing objects and storing the number of comparisons made */
public class Comparator<T extends Comparable<T>> implements java.util.Comparator<T> {

    private final java.util.Comparator<T> c;
    private long comparisons = 0;
    private long snapshot = 0;

    public static <T extends Comparable<T>> Comparator<T> naturalOrder() {
        return new Comparator<T>(java.util.Comparator.naturalOrder());
    }

    public Comparator(java.util.Comparator<T> c) {
        this.c = c;
    }

    public Comparator<T> reversed() {
        return new Comparator<>(this.c.reversed());
    }

    public int compare(T a, T b) {
        comparisons++;
        return c.compare(a, b);
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getComparisonsSnapshot() {
        long oldSnapshot = snapshot;
        snapshot = comparisons;
        return comparisons - oldSnapshot;
    }
}
