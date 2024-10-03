package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.ex1.Ex1Particle;
import org.example.interfaces.Params;

@AllArgsConstructor
@Getter
public abstract class Particle {
    private double r0;
    private double r1;
    private double r2;
    private double r3;
    private double r4;
    private double r5;
    private double time;
    private double dt;

    @Setter
    protected static Params params;

    public Particle(double r0, double r1, double time, double dt){
        this.r0 = r0;
        this.r1 = r1;
        this.time = time;
        this.dt = dt;
    }

    public double getPosition(){
        return r0;
    }

    public double getVelocity(){
        return r1;
    }

    protected abstract double getAcceleration();
}

