package org.example.ex2;

import lombok.Setter;
import org.example.Particle;

import java.util.function.Function;

public class Ex2Particle extends Particle {
    @Setter
    protected static Ex2Params params;

    private final static Function<Double, Double> armonicForceFormula = t -> params.A() * Math.cos(t * Ex2Particle.w());

    private static double w(){
        if (params == null){
            throw new IllegalStateException("Params not set");
        }
        return params.k() / params.m(); // todo check if correct
    }

    public Ex2Particle(double time, double dt, double position, double velocity) {
        super(time, dt, position, velocity);
    }

    private Ex2Particle createNextParticle(double position, double velocity){
        return new Ex2Particle(this.getTime() + this.getDt(), this.getDt(), position, velocity);
    }

    public Ex2Particle createNextVerlet(Ex2Particle previous, Ex2Particle currentLeft, Ex2Particle currentRight){
        double nextPosition = 2 * this.getPosition() - previous.getPosition() + Math.pow(this.getDt(), 2) * getAcceleration(currentLeft, currentRight);
        double nextVelocity = (nextPosition - previous.getPosition()) / (2 * this.getDt());

        return this.createNextParticle(nextPosition, nextVelocity);
    }

    private double getAcceleration(Particle leftParticle, Particle rightParticle){
        double time = 0;
        double force;

        double springForce = 0;

        if (leftParticle == null){
            force = armonicForceFormula.apply(time);
        } else if (rightParticle == null) {
            force = 0;
        } else {
            force = params.k() * (leftParticle.getPosition() - this.getPosition()) + params.k() * (rightParticle.getPosition() - this.getPosition());
        }

        return force / params.m();
    }
}
