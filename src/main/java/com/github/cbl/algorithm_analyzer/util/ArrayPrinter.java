package com.github.cbl.algorithm_analyzer.util;

import static com.diogonunes.jcolor.Ansi.*;
import static com.diogonunes.jcolor.Attribute.*;

import com.diogonunes.jcolor.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/** Utility class for pretty-printing arrays */
public class ArrayPrinter {

    private static final Attribute[][] COLORS = {
        {
            BLACK_TEXT(), BACK_COLOR(196),
        },
        {
            BLACK_TEXT(), BACK_COLOR(82),
        },
        {
            BLACK_TEXT(), BACK_COLOR(226),
        },
        {
            BLACK_TEXT(), BACK_COLOR(208),
        },
        {
            BLACK_TEXT(), BACK_COLOR(165),
        },
    };

    /**
     * Pretty-formats an array to a 'list of fields' w/ ASCII symbols
     *
     * @param arr the array
     * @return a string representing the array
     */
    public static String toString(Object[] arr) {
        return toString(arr, new int[0]);
    }

    /**
     * Pretty-formats an array to a 'list of fields' w/ ASCII symbols
     *
     * @param arr the array
     * @param colors colors of values. Shall be in (0,5]
     * @return a string representing the array
     */
    public static String toString(Object[] arr, int[] colors) {
        List<Integer> widths = new ArrayList<>();

        final StringJoiner arrSj = new StringJoiner("|", "|", "|");
        for (int i = 0; i < arr.length; i++) {
            Object el = arr[i];
            var color =
                    i >= colors.length
                            ? new Attribute[] {}
                            : getColor(colors[i]);
            final String s = " " + el.toString() + " ";
            widths.add(s.length());
            arrSj.add(colorize(s, color));
        }

        final StringJoiner sj = new StringJoiner("\n");
        sj.add(upperBorder(widths));
        sj.add(arrSj.toString());
        sj.add(lowerBorder(widths));

        return sj.toString();
    }

    private static Attribute[] getColor(int i) {
        if (i == -1) {
            return new Attribute[] {};
        } else {
            return COLORS[i % COLORS.length];
        }
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
