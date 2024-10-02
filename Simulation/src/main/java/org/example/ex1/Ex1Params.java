package org.example.ex1;

import org.example.interfaces.Params;

import java.util.List;

public record Ex1Params(
        double k, // Spring constant
        double y, // Damping constant
        double m, // Mass of the particle
        double r0, // Initial position
        double A, // Constant to calculate initial velocity
        List<Double> dts, // Time steps
        double tf // Final time
) implements Params {}
