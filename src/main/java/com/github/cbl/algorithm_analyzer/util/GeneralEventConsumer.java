package com.github.cbl.algorithm_analyzer.util;

import java.util.ArrayList;
import java.util.List;

import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.EventVisitor;

public class GeneralEventConsumer implements EventConsumer<Event> {

    private final List<Event> events = new ArrayList<>();

    @Override
    public void accept(Event e) {
        events.add(e);
    }

    @Override
    public void visitEvents(EventVisitor visitor) {
        events.forEach(e -> e.accept(visitor));
        
    }

}
