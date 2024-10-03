package org.example.ex1;

import org.example.Particle;
import org.example.interfaces.Results;

import java.util.List;

public record Ex1Results (
        Ex1Params params,
        List<ResultsForDt> results
) implements Results {}

record ResultsForDt(
        double dt,
        List<Ex1Particle> analytical,
        List<Ex1Particle> beeman,
        List<Ex1Particle> gear5,
        List<Ex1Particle> verlet
) {}