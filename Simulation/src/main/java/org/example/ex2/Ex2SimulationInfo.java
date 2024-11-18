package org.example.ex2;

import java.util.List;

public record Ex2SimulationInfo(
    int N, // Number of particles
    double m, // Mass of particles
    double A, // Amplitude of the armonic force
    double l, // Initial between the particles
    double k,
    double w,
    double tf, // Final time
    int saveInteval // Save every n steps
) {}
