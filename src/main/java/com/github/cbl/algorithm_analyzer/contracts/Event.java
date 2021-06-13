package com.github.cbl.algorithm_analyzer.contracts;

public interface Event {

    default void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
