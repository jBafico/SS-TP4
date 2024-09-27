package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Particle {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double mass;
    private double force;
}
