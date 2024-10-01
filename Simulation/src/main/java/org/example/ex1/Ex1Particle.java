package org.example.ex1;

import lombok.Getter;
import org.example.Particle;

@Getter
public class Ex1Particle extends Particle {
    // Initial particle
    public Ex1Particle(Ex1Params params) {
        super(params.r0(), -1 * params.A() * params.y() / (2 * params.m()));
    }

    // Iteration of particle
    public Ex1Particle(double position, double velocity) {
        super(position, velocity);
    }

    public double getAcceleration(Ex1Params params){
        return -((params.k() * this.getPosition()) + (params.y() * this.getVelocity())) / params.m();
    }
}
