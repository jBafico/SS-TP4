package org.example.ex1;

import org.example.Particle;
import org.example.interfaces.Results;

import java.util.List;
import java.util.Map;

public record Ex1Results (
        Ex1Params params,
        Map<Integer, List<ResultsForDt>> resultsByRepetitionNo
) implements Results {}
