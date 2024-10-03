package org.example.ex1;

import lombok.Setter;
import org.example.Particle;

public class Ex1Particle extends Particle {
    @Setter
    protected static Ex1Params params;

    // Initial particle
    public Ex1Particle(double dt) {
        super(0, dt, params.r0(), -1 * params.A() * params.y() / (2 * params.m()));
    }

    private Ex1Particle(double time, double dt, double position, double velocity){
        super(time, dt, position, velocity);
    }

    private Ex1Particle(double time, double dt, double r0, double r1, double r2, double r3, double r4, double r5){
        super(time, dt, r0, r1, r2, r3, r4, r5);
    }

    private Ex1Particle createNextParticle(double position, double velocity){
        return new Ex1Particle(this.getTime() + this.getDt(), this.getDt(), position, velocity);
    }

    private Ex1Particle createNextParticle(double r0, double r1, double r2, double r3, double r4, double r5){
        return new Ex1Particle(this.getTime() + this.getDt(), this.getDt(), r0, r1, r2, r3, r4, r5);
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
        return new Ex1Particle(this.getTime() - this.getDt(), this.getDt(), eulerPreviousPosition, -1);
    }

    public Ex1Particle createNextBeeman(Ex1Particle previousState){
        // Calculate next position
        double nextPosition = this.getPosition() + this.getVelocity() * this.getDt() + (2.0 / 3.0) * this.getAcceleration() * Math.pow(this.getDt(), 2) - (1.0 / 6.0) * previousState.getAcceleration() * Math.pow(this.getDt(), 2);
        // Predict next velocity
        double nextVelocityPredicted = this.getVelocity() + (3.0 / 2.0) * this.getAcceleration() * this.getDt() - (1.0 / 2.0) * previousState.getAcceleration() * this.getDt();
        // Create next particle with predicted velocity
        Ex1Particle nextParticlePredicted = new Ex1Particle(this.getTime() + this.getDt(), this.getDt(), nextPosition, nextVelocityPredicted);
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

    public Ex1Particle createNextGear(){
        // Step 1 of gear: predict
        double predictedR0 = this.gearPredictNextR0();
        double predictedR1 = this.gearPredictNextR1();
        double predictedR2 = this.gearPredictNextR2();
        double predictedR3 = this.gearPredictNextR3();
        double predictedR4 = this.gearPredictNextR4();
        double predictedR5 = this.gearPredictNextR5();

        // Step 2 of gear: evaluate and find correction factor
        double accelerationWithPredictedValues = this.createNextParticle(predictedR0, predictedR1).calcR2();
        double deltaA = accelerationWithPredictedValues - predictedR2;
        double deltaR2 = deltaA * Math.pow(this.getDt(), 2) / 2;

        // Step 3 of gear: correct
        double[] gearCoefficients = {3.0 / 16.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0, 1.0 / 60.0};
        double correctedR0 = predictedR0 + gearCoefficients[0] * deltaR2;
        double correctedR1 = predictedR1 + gearCoefficients[1] * deltaR2 / this.getDt();
        double correctedR2 = predictedR2 + gearCoefficients[2] * deltaR2 * 2 / Math.pow(this.getDt(), 2);
        double correctedR3 = predictedR3 + gearCoefficients[3] * deltaR2 * 6 / Math.pow(this.getDt(), 3);
        double correctedR4 = predictedR4 + gearCoefficients[4] * deltaR2 * 24 / Math.pow(this.getDt(), 4);
        double correctedR5 = predictedR5 + gearCoefficients[5] * deltaR2 * 120 / Math.pow(this.getDt(), 5);

        return this.createNextParticle(correctedR0, correctedR1, correctedR2, correctedR3, correctedR4, correctedR5);
    }

    // Gear helpers
    @Override
    protected double calcR5(){
        if (this.getR5() != null) {
            return this.getR5();
        }
        return - (params.k() * this.calcR3() + params.y() * this.calcR4()) / params.m();
    }

    @Override
    protected double calcR4(){
        if (this.getR4() != null) {
            return this.getR4();
        }
        return - (params.k() * this.calcR2() + params.y() * this.calcR3()) / params.m();
    }

    @Override
    protected double calcR3(){
        if (this.getR3() != null) {
            return this.getR3();
        }
        return - (params.k() * this.getR1() + params.y() * this.calcR2()) / params.m();
    }

    private double gearPredictNextR5(){
        return this.calcR5();
    }
    private double gearPredictNextR4(){
        return this.calcR4() + this.calcR5() * this.getDt();
    }
    private double gearPredictNextR3(){
        return this.calcR3() + this.calcR4() * this.getDt() + this.calcR5() * Math.pow(this.getDt(), 2) / 2;
    }
    private double gearPredictNextR2(){
        return this.calcR2() + this.calcR3() * this.getDt() + this.calcR4() * Math.pow(this.getDt(), 2) / 2 + this.calcR5() * Math.pow(this.getDt(), 3) / 6;
    }
    private double gearPredictNextR1(){
        return this.getR1() + this.calcR2() * this.getDt() + this.calcR3() * Math.pow(this.getDt(), 2) / 2 + this.calcR4() * Math.pow(this.getDt(), 3) / 6 + this.calcR5() * Math.pow(this.getDt(), 4) / 24;
    }
    private double gearPredictNextR0(){
        return this.getR0() + this.getR1() * this.getDt() + this.calcR2() * Math.pow(this.getDt(), 2) / 2 + this.calcR3() * Math.pow(this.getDt(), 3) / 6 + this.calcR4() * Math.pow(this.getDt(), 4) / 24 + this.calcR5() * Math.pow(this.getDt(), 5) / 120;
    }
}

