package com.github.cbl.algorithm_analyzer.util;

import java.util.Collections;
import java.util.StringJoiner;

/** Utility class for pretty-printing tables */
public class TablePrinter {

    /**
     * Pretty-formats an table to a 'list of fields' w/ ASCII symbols
     *
     * @param arr the table
     * @return a string representing the table
     */
    public static String toString(Object[][] table) {
        int[] maxWidths = new int[table[0].length];
        int width;

        for (int r = 0; r < table.length; r++) {
            for (int c = 0; c < table[r].length; c++) {
                width = table[r][c].toString().length();
                if ((width + 2) > maxWidths[c]) {
                    maxWidths[c] = width + 2;
                }
            }
        }

        StringJoiner rowSj, tableSj = new StringJoiner("\n" + middleBorder(maxWidths) + "\n");

        for (int r = 0; r < table.length; r++) {
            rowSj = new StringJoiner("|", "|", "|");
            for (int c = 0; c < table[r].length; c++) {
                final String s = " " + table[r][c].toString() + " ";
                rowSj.add(s + String.join("", Collections.nCopies(maxWidths[c] - s.length(), " ")));
            }
            tableSj.add(rowSj.toString());
        }

        final StringJoiner sj = new StringJoiner("\n");
        sj.add(upperBorder(maxWidths));
        sj.add(tableSj.toString());
        sj.add(lowerBorder(maxWidths));

        return sj.toString();
    }

    private static String upperBorder(int[] columnWidths) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnWidths.length; i++) {
            sj.add(i == 0 ? "┌" : "┬");
            sj.add("─".repeat(columnWidths[i]));
            sj.add(i == columnWidths.length - 1 ? "┐" : "");
        }

        return sj.toString();
    }

    private static String middleBorder(int[] columnWidths) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnWidths.length; i++) {
            sj.add(i == 0 ? "├" : "┼");
            sj.add("─".repeat(columnWidths[i]));
            sj.add(i == columnWidths.length - 1 ? "┤" : "");
        }

        return sj.toString();
    }

    private static String lowerBorder(int[] columnWidths) {
        final StringJoiner sj = new StringJoiner("");

        for (int i = 0; i < columnWidths.length; i++) {
            sj.add(i == 0 ? "└" : "┴");
            sj.add("─".repeat(columnWidths[i]));
            sj.add(i == columnWidths.length - 1 ? "┘" : "");
        }

        return sj.toString();
    }
}
