package org.example.ex2;

import lombok.Setter;
import org.example.Particle;

import java.util.function.BiFunction;


public class Ex2Particle extends Particle {
    @Setter
    protected static Ex2Params params;

    private final double w;
    private final double k;
    private final double xPosition;

    public Ex2Particle(double time, double dt, double position, double velocity, double xPosition, double w, double k) {
        super(time, dt, position, velocity);
        this.w = w;
        this.k = k;
        this.xPosition = xPosition;
    }

    private Ex2Particle createNextParticle(double position, double velocity){
        return new Ex2Particle(this.getTime() + this.getDt(), this.getDt(), position, velocity, this.xPosition, this.w, this.k);
    }

    public Ex2Particle createNextVerlet(Ex2Particle previous, Ex2Particle currentLeft, Ex2Particle currentRight){
        double nextPosition;
        double nextVelocity;
        if (currentLeft == null){ // We are in the armonic force particle
            double nextTime = this.getTime() + this.getDt();
            nextPosition = params.A() * Math.sin(nextTime * this.w);
            nextVelocity = 0; // It is not 0 but we are not using it
        } else if (currentRight == null){ // We are in the fixed particle (position and velocity are always 0)
            nextPosition = 0;
            nextVelocity = 0;
        } else { // We are in the middle of the chain
            nextPosition = 2 * this.getPosition() - previous.getPosition() + Math.pow(this.getDt(), 2) * getAcceleration(currentLeft, currentRight);
            nextVelocity = (nextPosition - previous.getPosition()) / (2 * this.getDt());
        }

        return this.createNextParticle(nextPosition, nextVelocity);
    }

    private double getAcceleration(Particle leftParticle, Particle rightParticle){
        double force;

        if (leftParticle == null || rightParticle == null){
            throw new RuntimeException("The left and right particles must be defined");
        }

        force = this.k * (leftParticle.getPosition() - this.getPosition()) + this.k * (rightParticle.getPosition() - this.getPosition());

        return force / params.m();
    }
}
