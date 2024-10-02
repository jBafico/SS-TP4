package org.example.ex1;

import lombok.Setter;
import org.example.Particle;

public class Ex1Particle extends Particle {
    @Setter
    private static Ex1Params params;

    // Initial particle
    public Ex1Particle() {
        super(params.r0(), -1 * params.A() * params.y() / (2 * params.m()), 0);
    }

    // Iteration of particle
    public Ex1Particle(double position, double velocity, double time) {
        super(position, velocity, time);
    }

    public double getAcceleration(){
        return -((params.k() * this.getPosition()) + (params.y() * this.getVelocity())) / params.m();
    }

    public static double getAnalyticalPosition(double t){
        return params.A() * Math.exp(-params.y() * t / (2 * params.m())) * Math.cos(Math.sqrt(params.k() / params.m() - Math.pow(params.y() / (2 * params.m()), 2)) * t);
    }

    public static Ex1Particle getEulerPreviousParticle(Ex1Particle currentState, double dt){
        /* Use inverse euler to approximate a particle before the initial particle */
        double eulerPreviousPosition = currentState.getPosition() - dt * currentState.getVelocity() + Math.pow(dt, 2) * currentState.getAcceleration() / 2;

        return new Ex1Particle(eulerPreviousPosition, -1, currentState.getTime() - dt);
    }

    public static Ex1Particle getNextBeeman(Ex1Particle previousState, Ex1Particle currentState, double dt){
        // Calculate next position
        double nextPosition = currentState.getPosition() + currentState.getVelocity() * dt + (2.0 / 3.0) * currentState.getAcceleration() * Math.pow(dt, 2) - (1.0 / 6.0) * previousState.getAcceleration() * Math.pow(dt, 2);
        // Calculate next velocity
        double nextVelocity = currentState.getVelocity() + (1.0 / 3.0) * currentState.getAcceleration() * dt + (5.0 / 6.0) * currentState.getAcceleration() * dt - (1.0 / 6.0) * previousState.getAcceleration() * dt; // fixme

        return new Ex1Particle(nextPosition, nextVelocity, currentState.getTime() + dt);
    }

    public static Ex1Particle getNextGear(Ex1Particle currentState, double dt){
        return null;
    }

    public static Ex1Particle getNextVerlet(Ex1Particle previousState, Ex1Particle currentState, double dt){
        // Calculate next position
        double nextPosition = 2 * currentState.getPosition() - previousState.getPosition() + Math.pow(dt, 2) * currentState.getAcceleration();
        // Calculate next velocity
        double nextVelocity = (nextPosition - previousState.getPosition()) / (2 * dt);

        return new Ex1Particle(nextPosition, nextVelocity, currentState.getTime() + dt);
    }

    // Gear helpers
//    private double gearPredictedR5()
}
