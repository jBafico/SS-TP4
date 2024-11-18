package org.example.ex2;

import java.util.List;

public record Ex2Results (
        Ex2SimulationInfo params,
        List<List<Ex2Particle>> results
){}
