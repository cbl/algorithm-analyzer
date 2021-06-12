package com.github.cbl.algorithm_analyzer.graphs.tiefensuche;

import com.github.cbl.algorithm_analyzer.contracts.Algorithm;
import com.github.cbl.algorithm_analyzer.contracts.Event;
import com.github.cbl.algorithm_analyzer.contracts.EventConsumer;
import com.github.cbl.algorithm_analyzer.contracts.Graph;

public class Tiefensuche implements Algorithm<Event, Tiefensuche.Data> {

    public static record Data (Graph<Integer,Void> graph, Integer startNode) {};

    public void run(EventConsumer<Event> logger, Data data) {
        // TODO: implement
    }

}