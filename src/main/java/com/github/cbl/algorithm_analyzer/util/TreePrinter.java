package com.github.cbl.algorithm_analyzer.util;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Utility class for pretty-printing trees */
public class TreePrinter {

    private static final int PRINT_WIDTH = 100;

    record ObjAndWidth(String string, int width) {}

    /**
     * Pretty-formats a tree to an ascii art like image
     *
     * @param t list of tree nodes in top-to-bottom, left-to-right order
     * @return a string representing the tree
     */
    public static String printTree(List<? extends Object> t) {
        StringJoiner sj = new StringJoiner("\n");

        int levelStart = 0;
        int levelEnd = 1;
        while (levelEnd <= t.size()) {
            var level = t.subList(levelStart, levelEnd);
            if (levelStart != 0) {
                sj.add(printArrows(level));
            }
            sj.add(printLevel(level));
            var size = levelEnd - levelStart;
            levelStart = levelEnd;
            levelEnd += size * 2;
        }

        return sj.toString();
    }

    private static String printLevel(List<? extends Object> t) {
        var depth = (int) Math.round(Math.log(t.size()) / Math.log(2));
        var positions = objectPositions(depth, 0, PRINT_WIDTH);
        var index = new AtomicInteger(0);
        return t.stream()
                .map(TreePrinter::printObject)
                .map(
                        o ->
                                padLeft(
                                        o.string(),
                                        positions.get(index.getAndIncrement()) - (o.width() / 2)))
                .collect(Collectors.reducing(TreePrinter::mergeMultilineStrings))
                .get();
    }

    private static String printArrows(List<? extends Object> t) {
        var depth = (int) Math.round(Math.log(t.size()) / Math.log(2));
        var positions = objectPositions(depth, 0, PRINT_WIDTH);
        var positionsUp = objectPositions(depth - 1, 0, PRINT_WIDTH);

        var s = " ".repeat(positions.get(0));
        for (int i = 0; i < t.size(); i++) {
            int j = i / 2;
            int l = Math.abs(positionsUp.get(j) - positions.get(i));
            if (null == t.get(i)) {
                s += " ".repeat(l);
            } else {
                if (i % 2 == 0) {
                    s += "┌";
                    s += "─".repeat(l - 1);
                } else {
                    s += "─".repeat(l - 1);
                    s += "┐";
                }
            }

            if (i % 2 == 1 && i < t.size() - 1) {
                s += " ".repeat(positions.get(i + 1) - positions.get(i));
            }
        }
        return s;
    }

    private static ObjAndWidth printObject(Object t) {
        if (t == null) {
            return new ObjAndWidth("", 0);
        } else {
            var s = t.toString();
            var box =
                    ("┌"
                            + "─".repeat(s.length() + 2)
                            + "┐"
                            + "\n"
                            + "| "
                            + s
                            + " |"
                            + "\n"
                            + "└"
                            + "─".repeat(s.length() + 2)
                            + "┘");

            return new ObjAndWidth(box, s.length() + 4);
        }
    }

    private static String padLeft(String s, int amount) {
        var ss = s.split("\n");
        for (int i = 0; i < ss.length; i++) {
            ss[i] = " ".repeat(amount) + ss[i];
        }
        return String.join("\n", ss);
    }

    private static String mergeMultilineStrings(String a, String b) {
        var ssa = a.split("\n");
        var ssb = b.split("\n");
        int linesNo = Math.max(ssa.length, ssb.length);
        var lines = new String[linesNo];

        for (int i = 0; i < linesNo; i++) {
            if (i >= ssa.length) {
                lines[i] = ssb[i];
            } else if (i >= ssb.length) {
                lines[i] = ssa[i];
            } else {
                lines[i] = mergeStrings(ssa[i], ssb[i]);
            }
        }

        return String.join("\n", lines);
    }

    private static String mergeStrings(String a, String b) {
        int lengtha = a.length();
        int lengthb = b.length();
        int length = Math.max(lengtha, lengthb);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            if (i >= a.length()) {
                sb.append(b.charAt(i));
            } else if (i >= b.length()) {
                sb.append(a.charAt(i));
            } else {
                char ca = a.charAt(i);
                char cb = b.charAt(i);

                if (ca == ' ') {
                    sb.append(cb);
                } else if (cb == ' ') {
                    sb.append(ca);
                } else {
                    sb.append(cb);
                }
            }
        }

        return sb.toString();
    }

    private static List<Integer> objectPositions(int depth, int rangeStart, int rangeEnd) {
        int middle = (rangeEnd + rangeStart) / 2;
        if (depth == 0) {
            return List.of(middle);
        } else {
            var ps1 = objectPositions(depth - 1, rangeStart, middle);
            var ps2 = objectPositions(depth - 1, middle, rangeEnd);

            return Stream.concat(ps1.stream(), ps2.stream()).collect(Collectors.toList());
        }
    }
}
