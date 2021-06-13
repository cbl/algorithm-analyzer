package com.github.cbl.algorithm_analyzer.contracts;

import com.github.cbl.algorithm_analyzer.sorts.bubblesort.BubbleSort;

/**
 * Event visitor interface
 * 
 * Used to act in some way on a collection of events with the ability
 * to handle each event differently based on its specific implementation.
 * 
 * @see https://en.wikipedia.org/wiki/Visitor_pattern
 */
public interface EventVisitor {
    
    default void visit(Event e) {}

    default <T> void visit(BubbleSort.PartialStateEvent<T> e) {
        visit((Event) e);
    }
}
