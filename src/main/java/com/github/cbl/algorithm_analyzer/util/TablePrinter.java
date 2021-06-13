package com.github.cbl.algorithm_analyzer.util;

import java.util.StringJoiner;
import java.util.Collections;

/** Utility class for pretty-printing tables */
public class TablePrinter {

    /**
     * Pretty-formats an table to a 'list of fields' w/ ASCII symbols
     *
     * @param arr the table
     * @return a string representing the table
     */
    public static String toString(Object[][] table) {
        Integer maxWidth = 0, width, columnCount = table[0].length;

        for (Object[] row : table) {
            for (Object column : row) {
                width = column.toString().length();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }

        StringJoiner rowSj, tableSj = new StringJoiner("\n" + middleBorder(maxWidth, columnCount) + "\n");

        for (Object[] row : table) {
            rowSj = new StringJoiner("|", "|", "|");
            for (Object column : row) {
                rowSj.add(column.toString()
                        + String.join("", Collections.nCopies(maxWidth - column.toString().length(), " ")));
            }
            tableSj.add(rowSj.toString());
        }

        final StringJoiner sj = new StringJoiner("\n");
        sj.add(upperBorder(maxWidth, columnCount));
        sj.add(tableSj.toString());
        sj.add(lowerBorder(maxWidth, columnCount));

        return sj.toString();
    }

    private static String upperBorder(Integer columnWidth, Integer columnCount) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnCount; i++) {
            sj.add(i == 0 ? "┌" : "┬");
            sj.add("─".repeat(columnWidth));
            sj.add(i == columnCount - 1 ? "┐" : "");
        }

        return sj.toString();
    }

    private static String middleBorder(Integer columnWidth, Integer columnCount) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnCount; i++) {
            sj.add(i == 0 ? "├" : "┼");
            sj.add("─".repeat(columnWidth));
            sj.add(i == columnCount - 1 ? "┤" : "");
        }

        return sj.toString();
    }

    private static String lowerBorder(Integer columnWidth, Integer columnCount) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnCount; i++) {
            sj.add(i == 0 ? "└" : "┴");
            sj.add("─".repeat(columnWidth));
            sj.add(i == columnCount - 1 ? "┘" : "");
        }

        return sj.toString();
    }
}
