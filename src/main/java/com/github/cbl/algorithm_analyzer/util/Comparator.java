package com.github.cbl.algorithm_analyzer.util;

/**
 * Utility class for comparing objects and storing the number of comparisons made
 */
public class Comparator {

    private long comparisons = 0;
    private long snapshot = 0;

    public <T extends Comparable<T>> int compare(T a, T b) {
        comparisons++;
        return a.compareTo(b);
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
