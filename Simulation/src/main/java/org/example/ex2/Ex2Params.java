package org.example.ex2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.interfaces.Params;

import java.util.List;

public record Ex2Params(
        int N, // Number of particles
        double m, // Mass of particles
        double k, // Spring constant
        double A, // Amplitude of the armonic force
        double l, // Initial between the particles
        List<Double> dts, // Time steps
        double tf, // Final time
        int repetitions // Number of repetitions
) implements Params {}