package org.example.ex2;
import org.example.Particle;
import org.example.interfaces.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ex2Simulation implements Simulation<Ex2Params, Ex2Results> {
    public Ex2Results run(Ex2Params params, String timestamp, String outputFilePath){
        // Set class variable params
        Ex2Particle.setParams(params);

        // Create results object
        Ex2Results results = new Ex2Results(params, new HashMap<>());

        // Run simulation for each repetition
        for (int repetitionNo = 0; repetitionNo < params.repetitions(); repetitionNo++) {
            List<ResultsForDt> resultsForRepetition = new ArrayList<>();

            // Run simulation for each dt
            for (double dt : params.dts()) {
                List<Ex2Particle> analytical = new ArrayList<>();
                List<Ex2Particle> beeman = new ArrayList<>();
                List<Ex2Particle> verlet = new ArrayList<>();
                List<Ex2Particle> gear5 = new ArrayList<>();
                for (double t = 0; t <= params.tf(); t += dt) {
                    // Add particles to list
                }

                // Save all results for this configuration
                resultsForRepetition.add(new ResultsForDt(dt, analytical, beeman, gear5, verlet));
            }
        }

        return results;
    }

    private List<Particle> generateEx2Particles(Ex2Params params) {
        List<Particle> generatedParticles = new ArrayList<>();

        // Generate the particles with initial force 0
        double positionIterator = 0;
        for (int i = 0; i < params.N(); i++) {
            // If we are in the last particle, add the armonic force
            boolean hasArmonicForce = i == params.N()-1;
//            generatedParticles.add(new Ex2Particle(positionIterator, 0, hasArmonicForce, params));
            positionIterator += params.l();
        }
        return generatedParticles;
    }
}
