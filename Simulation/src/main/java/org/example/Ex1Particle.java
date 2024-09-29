package org.example;

import lombok.Getter;

@Getter
public class Ex1Particle extends Particle {
    // Initial particle
    public Ex1Particle(Ex1Params params) {
        super(params.getR0(), -1 * params.getA() * params.getY() / (2 * params.getM()));
    }

    // Iteration of particle
    public Ex1Particle(double position, double velocity) {
        super(position, velocity);
    }

    public double getAcceleration(Ex1Params params){
        return -((params.getK() * this.getPosition()) + (params.getY() * this.getVelocity())) / params.getM();
    }
}
