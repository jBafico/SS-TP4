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
                List<List<Ex2Particle>> verlet = new ArrayList<>();
                for (double t = 0; t <= params.tf(); t += dt) {
                    if (t == 0) {
                        // Add initial particles to list
                        // We do it twice because we need two previous particles to calculate the next one with verlet
                        verlet.add(generateInitialEx2Particles(params, dt));
                        verlet.add(generateInitialEx2Particles(params, dt));
                    } else {
                        List<Ex2Particle> previousState = verlet.get(verlet.size() - 2);
                        List<Ex2Particle> currentState = verlet.getLast();
                        List<Ex2Particle> newState = new ArrayList<>();
                        for (int i = 0; i < verlet.getLast().size(); i++) {
                            Ex2Particle previous = previousState.get(i);
                            Ex2Particle current = currentState.get(i);
                            Ex2Particle currentLeft = i == 0 ? null : currentState.get(i - 1);
                            Ex2Particle currentRight = i == currentState.size() - 1 ? null : currentState.get(i + 1);
                            newState.add(current.createNextVerlet(previous, currentLeft, currentRight));
                        }
                        verlet.add(newState);
                    }
                }
                // Remove the initial particles that we added twice
                verlet.removeFirst();
                // Save all results for this configuration
                resultsForRepetition.add(new ResultsForDt(dt, verlet));
            }
            results.resultsByRepetitionNo().put(repetitionNo, resultsForRepetition);
        }

        return results;
    }

    private List<Ex2Particle> generateInitialEx2Particles(Ex2Params params, double dt) {
        List<Ex2Particle> generatedParticles = new ArrayList<>();

        double timeIterator = 0;
        double positionIterator = 0;
        for (int i = 0; i <= params.N(); i++) {
            generatedParticles.add(new Ex2Particle(timeIterator, dt, positionIterator, 0));
            positionIterator += params.l();
            timeIterator += dt;
        }
        return generatedParticles;
    }
}
