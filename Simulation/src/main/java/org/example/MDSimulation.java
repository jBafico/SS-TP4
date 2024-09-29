package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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

    public void start(GlobalParams params){
        // Ex1
        for (int i = 0, j = 6; j >= 2; j--, i++) {
            double dt = Math.pow(10, -j);
            try (BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(String.format("verlet_%d.txt", i)),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )){
                writer.write("{\n");
                Ex1Particle p = new Ex1Particle(params.getEx1Params());
                writer.write("\n}");
            } catch (IOException e) {
                throw new RuntimeException("Could not write files.");
            }

        }
    }
}
