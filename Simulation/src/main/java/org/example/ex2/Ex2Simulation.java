package org.example.ex2;

import org.example.MemoryFriendlyArrayList;

import java.util.*;

public class Ex2Simulation {
    public Ex2Results run(Ex2SimulationInfo params){
        // Set class variable params
        Ex2Particle.setParams(params);

        double dt = 1 / (params.w() * 100);
        MemoryFriendlyArrayList<List<Ex2Particle>> verlet = new MemoryFriendlyArrayList<>(params.saveInteval(), 5);
        for (double t = 0; t <= params.tf(); t += dt) {
            if (t == 0) {
                // Add initial particles to list
                // We do it twice because we need two previous particles to calculate the next one with verlet
                verlet.add(generateInitialEx2Particles(params, dt));
                verlet.add(generateInitialEx2Particles(params, dt));
            } else {
                List<Ex2Particle> previousState = verlet.getSecondLast();
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

        return new Ex2Results(params, verlet.getList());
    }

    private List<Ex2Particle> generateInitialEx2Particles(Ex2SimulationInfo params, double dt) {
        List<Ex2Particle> generatedParticles = new ArrayList<>();

        double xPositionIterator = 0;
        for (int i = 0; i <= params.N(); i++) {
            generatedParticles.add(new Ex2Particle(0, dt, 0, 0, xPositionIterator, params.w(), params.k()));
            xPositionIterator += params.l();
        }
        return generatedParticles;
    }
}
