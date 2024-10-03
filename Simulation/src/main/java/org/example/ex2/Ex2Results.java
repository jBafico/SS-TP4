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
        List<Ex2Particle> analytical,
        List<Ex2Particle> beeman,
        List<Ex2Particle> gear5,
        List<Ex2Particle> verlet
) {}
