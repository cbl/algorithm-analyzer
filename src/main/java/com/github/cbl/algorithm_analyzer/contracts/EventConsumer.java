package com.github.cbl.algorithm_analyzer.contracts;

import java.util.function.Consumer;

public interface EventConsumer<T extends Event> extends Consumer<T> {

    void visitEvents(EventVisitor visitor);
}
