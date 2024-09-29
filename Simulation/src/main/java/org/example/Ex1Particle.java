package org.example;

import lombok.Getter;

@Getter
public class Ex1Particle extends Particle {
    public Ex1Particle(double position, Ex1Params params) {
        super(position, -1 * params.getA() * params.getY() / (2 * params.getM()));
    }

    public double getForce(Ex1Params params){
        return -((params.getK() * this.getPosition()) + (params.getY() * this.getVelocity())) / params.getM();
    }
}
