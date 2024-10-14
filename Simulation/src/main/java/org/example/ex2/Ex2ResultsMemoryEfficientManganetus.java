package org.example.ex2;

import org.example.interfaces.Results;

import java.util.TreeMap;

public record Ex2ResultsMemoryEfficientManganetus (
        Ex2Params params,
        TreeMap<Double, TreeMap<Double, Double>> resultsByKAndW
) implements Results {}
