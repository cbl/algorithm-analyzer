package com.github.cbl.algorithm_analyzer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/** Utility class for pretty-printing arrays */
public class ArrayPrinter {

    /**
     * Pretty-formats an array to a 'list of fields' w/ ASCII symbols
     * 
     *
     * @param arr the array
     * @return a string representing the array
     */
    public static String toString(Object[] arr) {
        List<Integer> widths = new ArrayList<>();
        final StringJoiner arrSj = new StringJoiner("|", "|", "|");
        for (Object el : arr) {
            final String s = " " + el.toString() + " ";
            widths.add(s.length());
            arrSj.add(s);
        }

        final StringJoiner sj = new StringJoiner("\n");
        sj.add(upperBorder(widths));
        sj.add(arrSj.toString());
        sj.add(lowerBorder(widths));

        return sj.toString();
    }

    private static String upperBorder(List<Integer> widths) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < widths.size(); i++) {
            Integer w = widths.get(i);
            sj.add(i == 0 ? "┌" : "┬");
            sj.add("─".repeat(w));
            sj.add(i == widths.size() - 1 ? "┐" : "");
        }
        return sj.toString();
    }

    private static String lowerBorder(List<Integer> widths) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < widths.size(); i++) {
            Integer w = widths.get(i);
            sj.add(i == 0 ? "└" : "┴");
            sj.add("─".repeat(w));
            sj.add(i == widths.size() - 1 ? "┘" : "");
        }
        return sj.toString();
    }
}
