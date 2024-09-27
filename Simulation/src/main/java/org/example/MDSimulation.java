package org.example;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MDSimulation {
    private double particleInitialForce(Ex2Params params, Particle leftParticle, Particle particle, Particle rightParticle, Function<Double, Double> armonicForceFormula){
        double time = 0;
        double armonicForce = armonicForceFormula != null ? armonicForceFormula.apply(time) : 0;

        double springForce = 0;
        if (leftParticle != null) {
            springForce += -1 * params.getK() * (particle.getPosition() - leftParticle.getPosition());
        }

        if (rightParticle != null) {
            springForce += -1 * params.getK() * (particle.getPosition() - rightParticle.getPosition());
        }

        return springForce + armonicForce;
    }

    private List<Particle> generateParticles(Ex2Params params) {
        List<Particle> generatedParticles = new ArrayList<>();

        // Generate the particles with force 0
        double positionIterator = 0;
        for (int i = 0; i < params.getN(); i++) {
            generatedParticles.add(new Particle(positionIterator, 0, params.getM(), 0));
        }

        // Add the initial force for each particle
        for (int i = 0; i < params.getN(); i++) {
            // Particle to calculate the initial force
            Particle particle = generatedParticles.get(i);
            // If we have a left particle, add it
            Particle leftParticle = i == params.getN()-1 ? null : generatedParticles.get(i + 1);
            // If we have a right particle, add it
            Particle rightParticle = i == 0 ? null : generatedParticles.get(i - 1);
            // If we are in the last particle, add the armonic force
            Function<Double, Double> armomicForce = i != params.getN()-1 ? null : x -> params.getA() * Math.cos(0);
            // Calculate the initial force for the particle
            particle.setForce(particleInitialForce(params, leftParticle, particle, rightParticle, armomicForce));
        }
        return generatedParticles;
    }

    public void start(FileWriter writer, GlobalParams params){
        // Generate particle list with initial positions, velocities and forces
        List<Particle> particles = generateParticles(params.getEx2Params());
    }
}
