package org.example.ex2;

import org.example.interfaces.Results;

import java.util.List;
import java.util.Map;

public record Ex2Results (
        Ex2Params params,
        Map<Integer, List<ResultsForDt>> resultsByRepetitionNo
) implements Results {}

record ResultsForDt(
        double dt,
        List<List<Ex2Particle>> verlet
) {}
