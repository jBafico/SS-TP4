package org.example;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MDSimulation {
    /**
     * Generate the particles with the initial positions, velocities and forces
     * @param params: Ex2Params object with the parameters for the simulation
     * @return List of Particle objects with the initial positions, velocities and forces
     */
    private List<Particle> generateEx2Particles(Ex2Params params) {
        List<Particle> generatedParticles = new ArrayList<>();

        // Generate the particles with initial force 0
        double positionIterator = 0;
        for (int i = 0; i < params.getN(); i++) {
            // If we are in the last particle, add the armonic force
            boolean hasArmonicForce = i == params.getN()-1;
            generatedParticles.add(new Ex2Particle(positionIterator, 0, hasArmonicForce, params));
            positionIterator += params.getL();
        }

        return generatedParticles;
    }

    public void start(FileWriter writer, GlobalParams params){
        // Generate particle list with initial positions, velocities and forces
        List<Particle> particles = generateEx2Particles(params.getEx2Params());
    }
}
