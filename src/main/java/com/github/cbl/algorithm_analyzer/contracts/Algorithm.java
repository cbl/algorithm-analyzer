package com.github.cbl.algorithm_analyzer.contracts;

@FunctionalInterface
public interface Algorithm<E extends Event, D> {
    /**
     * Execute an arbritary algorithm
     * @param events a consumer of possible events
     * @param data the run time data for this execution
     */
    public void run(EventConsumer<E> events, D data);
}