package org.example.ex2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.interfaces.Params;

public record Ex2Params(
    int N, // Number of particles
    double m, // Mass of particles
    double k, // Spring constant
    double A, // Amplitude of the armonic force
    double l, // Initial distance of the particles
    double dt, // Time step
    double tf // Final time
) implements Params {}
