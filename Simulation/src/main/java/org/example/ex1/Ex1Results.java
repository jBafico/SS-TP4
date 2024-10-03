package org.example.ex1;

import org.example.Particle;
import org.example.interfaces.Results;

import java.util.List;
import java.util.Map;

public record Ex1Results (
        Ex1Params params,
        Map<Integer, List<ResultsForDt>> resultsByRepetitionNo
) implements Results {}

record ResultsForDt(
        double dt,
        List<Ex1Particle> analytical,
        List<Ex1Particle> beeman,
        List<Ex1Particle> gear5,
        List<Ex1Particle> verlet
) {}