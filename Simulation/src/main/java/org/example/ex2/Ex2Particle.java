package org.example.ex2;

import lombok.Setter;
import org.example.Particle;

import java.util.function.Function;

public class Ex2Particle extends Particle {
    @Setter
    protected static Ex2Params params;

    private final Function<Double, Double> armonicForceFormula;
    private final double w;

    public Ex2Particle(double position, double velocity, boolean hasArmonicForce, double time, double dt) {
        super(time, dt, position, velocity);
        w = Math.sqrt(params.k() / params.m());
        if (hasArmonicForce) {
            this.armonicForceFormula = t -> params.A() * Math.cos(t * w);
        } else {
            this.armonicForceFormula = null;
        }
    }

    private double getAcceleration(Particle leftParticle, Particle rightParticle){
        double time = 0;
        double armonicForce = armonicForceFormula != null ? armonicForceFormula.apply(time) : 0;

        double springForce = 0;
        if (leftParticle != null) {
            springForce += -1 * params.k() * (this.getPosition() - leftParticle.getPosition());
        }

        if (rightParticle != null) {
            springForce += -1 * params.k() * (this.getPosition() - rightParticle.getPosition());
        }

        return (springForce + armonicForce) / params.m();
    }
}
