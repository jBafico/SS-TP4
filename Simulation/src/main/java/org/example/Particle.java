package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Particle {
    private double position;
    private double velocity;
    private double mass;
    private double force;
}
