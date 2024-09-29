package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Ex2Params {
    private int N; // Number of particles
    private double m; // Mass of particles
    private double k; // Spring constant
    private double A; // Amplitude of the armonic force
    private double l; // Initial distance of the particles
    private double dt; // Time step
    private double tf; // Final time
}
