package org.example.ex2;

import lombok.Setter;
import org.example.Particle;

import java.util.function.BiFunction;


public class Ex2Particle extends Particle {
    @Setter
    protected static Ex2Params params;

    private final static BiFunction<Double, Double, Double> armonicForceFormula = (t, w) -> params.A() * Math.cos(t * w);

    private final double w;
    private final double k;

    public Ex2Particle(double time, double dt, double position, double velocity, double w, double k) {
        super(time, dt, position, velocity);
        this.w = w;
        this.k = k;
    }

    private Ex2Particle createNextParticle(double position, double velocity){
        return new Ex2Particle(this.getTime() + this.getDt(), this.getDt(), position, velocity, this.w, this.k);
    }

    public Ex2Particle createNextVerlet(Ex2Particle previous, Ex2Particle currentLeft, Ex2Particle currentRight){
        double nextPosition = 2 * this.getPosition() - previous.getPosition() + Math.pow(this.getDt(), 2) * getAcceleration(currentLeft, currentRight);
        double nextVelocity = (nextPosition - previous.getPosition()) / (2 * this.getDt());

        return this.createNextParticle(nextPosition, nextVelocity);
    }

    private double getAcceleration(Particle leftParticle, Particle rightParticle){
        double time = this.getTime();
        double w = this.w;
        double force;

        if (leftParticle == null){
            force = armonicForceFormula.apply(time, w);
        } else if (rightParticle == null) {
            force = 0;
        } else {
            force = this.k * (leftParticle.getPosition() - this.getPosition()) + this.k * (rightParticle.getPosition() - this.getPosition());
        }

        return force / params.m();
    }
}
