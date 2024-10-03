package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.interfaces.Params;

@Getter
public abstract class Particle {
    private double time;
    private double dt;
    private double r0;
    private double r1;
    private Double r2;
    private Double r3;
    private Double r4;
    private Double r5;


    @Setter
    protected static Params params;

    public Particle(double time, double dt, double r0, double r1, double r2, double r3, double r4, double r5){
        this.time = time;
        this.dt = dt;
        this.r0 = r0;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }

    public Particle(double time, double dt, double r0, double r1){
        this.time = time;
        this.dt = dt;
        this.r0 = r0;
        this.r1 = r1;
        this.r2 = null;
        this.r3 = null;
        this.r4 = null;
        this.r5 = null;
    }

    public double getPosition(){
        return r0;
    }

    public double getVelocity(){
        return r1;
    }

    protected abstract double getAcceleration();
    protected double calcR2(){
        if (this.r2 == null){
            this.r2 = this.getAcceleration();
        }
        return r2;
    }
    protected abstract double calcR3();
    protected abstract double calcR4();
    protected abstract double calcR5();
}

