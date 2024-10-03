package org.example.ex1;

import lombok.Setter;
import org.example.Particle;

public class Ex1Particle extends Particle {
    @Setter
    protected static Ex1Params params;

    // Initial particle
    public Ex1Particle(double dt) {
        super(params.r0(), -1 * params.A() * params.y() / (2 * params.m()), 0, dt);
    }

    private Ex1Particle(double position, double velocity, double time, double dt){
        super(position, velocity, time, dt);
    }

    private Ex1Particle createNextParticle(double position, double velocity){
        return new Ex1Particle(position, velocity, this.getTime() + this.getDt(), this.getDt());
    }

    public double getAcceleration(){
        return -((params.k() * this.getPosition()) + (params.y() * this.getVelocity())) / params.m();
    }

    public Ex1Particle createNextAnalytical(){
        double nextTime = this.getTime() + this.getDt();
        double nextAnalyticalPosition = params.A() * Math.exp(-params.y() * nextTime / (2 * params.m())) * Math.cos(Math.sqrt(params.k() / params.m() - Math.pow(params.y() / (2 * params.m()), 2)) * nextTime);
        return this.createNextParticle(nextAnalyticalPosition, -1);
    }

    public Ex1Particle createEulerPreviousParticle(){
        /* Use inverse euler to approximate a particle before the initial particle */
        double eulerPreviousPosition = this.getPosition() - this.getDt() * this.getVelocity() + Math.pow(this.getDt(), 2) * this.getAcceleration() / 2;
        return new Ex1Particle(eulerPreviousPosition, -1, this.getTime() - this.getDt(), this.getDt());
    }

    public Ex1Particle createNextBeeman(Ex1Particle previousState){
        // Calculate next position
        double nextPosition = this.getPosition() + this.getVelocity() * this.getDt() + (2.0 / 3.0) * this.getAcceleration() * Math.pow(this.getDt(), 2) - (1.0 / 6.0) * previousState.getAcceleration() * Math.pow(this.getDt(), 2);
        // Predict next velocity
        double nextVelocityPredicted = this.getVelocity() + (3.0 / 2.0) * this.getAcceleration() * this.getDt() - (1.0 / 2.0) * previousState.getAcceleration() * this.getDt();
        // Create next particle with predicted velocity
        Ex1Particle nextParticlePredicted = new Ex1Particle(nextPosition, nextVelocityPredicted, this.getTime() + this.getDt(), this.getDt());
        // Correct next velocity
        double nextVelocityCorrected = this.getVelocity() + (1.0 / 3.0) * nextParticlePredicted.getAcceleration() * this.getDt() + (5.0 / 6.0) * this.getAcceleration() * this.getDt() - (1.0 / 6.0) * previousState.getAcceleration() * this.getDt();
        return this.createNextParticle(nextPosition, nextVelocityCorrected);
    }

    public Ex1Particle createNextVerlet(Ex1Particle previousState){
        // Calculate next position
        double nextPosition = 2 * this.getPosition() - previousState.getPosition() + Math.pow(this.getDt(), 2) * this.getAcceleration();
        // Calculate next velocity
        double nextVelocity = (nextPosition - previousState.getPosition()) / (2 * this.getDt());

        return this.createNextParticle(nextPosition, nextVelocity);
    }

//    public Ex1Particle getNextGear(){
//        GearParticle gearCurrentParticle = new GearParticle(
//            gearR5(currentState.getPosition(), currentState.getVelocity()),
//            gearR4(currentState.getPosition(), currentState.getVelocity()),
//            gearR3(currentState.getPosition(), currentState.getVelocity()),
//            gearR2(currentState.getPosition(), currentState.getVelocity()),
//            currentState.getVelocity(),
//            currentState.getPosition()
//        );
//
//        GearParticle gearPredictedNextParticle = new GearParticle(
//            gearPredictNextR5(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt),
//            gearPredictNextR4(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt),
//            gearPredictNextR3(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt),
//            gearPredictNextR2(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt),
//            gearPredictNextR1(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt),
//            gearPredictNextR0(gearCurrentParticle.getR0(), gearCurrentParticle.getR1(), dt)
//        );
//        double gearPredictedNextAcceleration = gearPredictedNextParticle.getR2();
//        double gearNextAcceleration = gearR2(gearPredictedNextParticle.getR0(), gearPredictedNextParticle.getR1());
//
//        return null;
//    }

    // Gear helpers
//    private static double gearR5(double r0, double r1){
//        return - (params.k() * gearR3(r0, r1) + params.y() * gearR4(r0, r1)) / params.m();
//    }
//    private static double gearR4(double r0, double r1){
//        return - (params.k() * gearR2(r0, r1) + params.y() * gearR3(r0, r1)) / params.m();
//    }
//    private static double gearR3(double r0, double r1){
//        return - (params.k() * r1 + params.y() * gearR2(r0, r1)) / params.m();
//    }
//    private static double gearR2(double r0, double r1){
//        return - (params.k() * r0 + params.y() * r1) / params.m();
//    }
//
//    private static double gearPredictNextR5(double r0, double r1, double dt){
//        return gearR5(r0, r1);
//    }
//    private static double gearPredictNextR4(double r0, double r1, double dt){
//        return gearR4(r0, r1) + gearR5(r0, r1) * dt;
//    }
//    private static double gearPredictNextR3(double r0, double r1, double dt){
//        return gearR3(r0, r1) + gearR4(r0, r1) * dt + gearR5(r0, r1) * Math.pow(dt, 2) / 2;
//    }
//    private static double gearPredictNextR2(double r0, double r1, double dt){
//        return gearR2(r0, r1) + gearR3(r0, r1) * dt + gearR4(r0, r1) * Math.pow(dt, 2) / 2 + gearR5(r0, r1) * Math.pow(dt, 3) / 6;
//    }
//    private static double gearPredictNextR1(double r0, double r1, double dt){
//        return r1 + gearR2(r0, r1) * dt + gearR3(r0, r1) * Math.pow(dt, 2) / 2 + gearR4(r0, r1) * Math.pow(dt, 3) / 6 + gearR5(r0, r1) * Math.pow(dt, 4) / 24;
//    }
//    private static double gearPredictNextR0(double r0, double r1, double dt){
//        return r0 + r1 * dt + gearR2(r0, r1) * Math.pow(dt, 2) / 2 + gearR3(r0, r1) * Math.pow(dt, 3) / 6 + gearR4(r0, r1) * Math.pow(dt, 4) / 24 + gearR5(r0, r1) * Math.pow(dt, 5) / 120;
//    }


}

