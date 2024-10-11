package org.example.ex2;
import org.example.interfaces.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex2Simulation implements Simulation<Ex2Params, Ex2Results> {
    public Ex2Results run(Ex2Params params, String timestamp, String outputFilePath){
        // Set class variable params
        Ex2Particle.setParams(params);

        // Create results object
        Ex2Results results = new Ex2Results(params, new HashMap<>());

        // Calculate results for each k
        params.kValues().forEach(k -> {
            // Calculate results for each w
            Map<Integer, List<List<Ex2Particle>>> resultsByW = new HashMap<>();
            for (int w = params.minW(); w <= params.maxW(); w += params.increaseW()){
                double dt = 1 / ((double)w * 100);
                List<List<Ex2Particle>> verlet = new ArrayList<>();
                for (double t = 0; t <= params.tf(); t += dt) {
                    if (t == 0) {
                        // Add initial particles to list
                        // We do it twice because we need two previous particles to calculate the next one with verlet
                        verlet.add(generateInitialEx2Particles(params, dt, w, k));
                        verlet.add(generateInitialEx2Particles(params, dt, w, k));
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

                // Add the results to the results object
                resultsByW.put(w, verlet);
            }
            results.resultsByKAndW().put(k, resultsByW);
        });

        return results;
    }

    private List<Ex2Particle> generateInitialEx2Particles(Ex2Params params, double dt, double w, double k) {
        List<Ex2Particle> generatedParticles = new ArrayList<>();

        double xPositionIterator = 0;
        for (int i = 0; i <= params.N(); i++) {
            generatedParticles.add(new Ex2Particle(0, dt, 0, 0, xPositionIterator, w, k));
            xPositionIterator += params.l();
        }
        return generatedParticles;
    }
}
