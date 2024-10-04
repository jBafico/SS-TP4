package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.interfaces.Params;

@Getter
public abstract class Particle {
    private final double time;
    private final double dt;
    private final double r0;
    private final double r1;


    @Setter
    protected static Params params;

    public Particle(double time, double dt, double r0, double r1){
        this.time = time;
        this.dt = dt;
        this.r0 = r0;
        this.r1 = r1;
    }

    public double getPosition(){
        return r0;
    }

    public double getVelocity(){
        return r1;
    }
}

