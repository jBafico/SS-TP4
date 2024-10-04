package org.example.ex2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.interfaces.Params;

import java.util.List;

public record Ex2Params(
        int N, // Number of particles
        double m, // Mass of particles
        double A, // Amplitude of the armonic force
        double l, // Initial between the particles
        List<Double> kValues, // List of k values
        int minW, // Minimum w
        int maxW, // Maximum w
        int increaseW, // Increase w
        double tf // Final time
) implements Params {}
