package org.example.ex1;

import org.example.interfaces.Results;
import org.example.State;

import java.util.List;

public record Ex1Results (
        Ex1Params params,
        List<ResultsForDt> results
) implements Results {}

record ResultsForDt(
        double dt,
        List<State> analytical,
        List<State> beeman,
        List<State> verlet,
        List<State> gear5
) {}