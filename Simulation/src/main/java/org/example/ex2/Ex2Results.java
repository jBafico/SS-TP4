package org.example.ex2;

import org.example.interfaces.Results;

import java.util.List;
import java.util.Map;

public record Ex2Results (
        Ex2Params params,
        Map<Double, Map<Integer, List<List<Ex2Particle>>>> resultsByKAndW
) implements Results {}
