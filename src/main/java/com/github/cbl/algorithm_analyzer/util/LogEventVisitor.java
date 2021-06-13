package com.github.cbl.algorithm_analyzer.util;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventVisitor;

public class LogEventVisitor implements EventVisitor {

    @Override
    public void visit(Event e) {
        System.out.println(e);
        System.out.println();
    }
}
