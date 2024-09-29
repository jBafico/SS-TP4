package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Ex1Params {
    private double k; // Spring constant
    private double y; // Damping constant
    private double m; // Mass of the particle
    private double r0; // Initial position
    private double A; // Constant to calculate initial velocity
    private double dt; // Time step
    private double tf; // Final time
}
