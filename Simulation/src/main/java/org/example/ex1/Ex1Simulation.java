package org.example.ex1;

import org.example.interfaces.Simulation;

import java.util.ArrayList;
import java.util.List;

public class Ex1Simulation implements Simulation<Ex1Params, Ex1Results> {
    @Override
    public Ex1Results run(Ex1Params params, String timestamp, String outputFilePath) {
        // Set class variable params
        Ex1Particle.setParams(params);

        // Create results object
        Ex1Results ex1Results = new Ex1Results(params, new ArrayList<>());

        // Run simulation for each dt
        for (double dt : params.dts()) {
            List<Ex1Particle> analytical = new ArrayList<>();
            List<Ex1Particle> beeman = new ArrayList<>();
            List<Ex1Particle> verlet = new ArrayList<>();
            List<Ex1Particle> gear5 = new ArrayList<>();
            for (double t = 0; t <= params.tf(); t += dt) {
                // Get analytical results
                double position = Ex1Particle.getAnalyticalPosition(t);
                analytical.add(new Ex1Particle(position, -1, t)); // -1 is a placeholder for velocity because the analytical solution does not provide velocity

                // Get beeman results


                // Get verlet results
                Ex1Particle previous = null;
                if (verlet.isEmpty()) {
                    // Create initial particle
                    verlet.add(new Ex1Particle());

                    // Use euler to approximate a particle before the initial particle
                    previous = Ex1Particle.getEulerPreviousParticle(verlet.getFirst(), dt);
                    verlet.addFirst(previous);

                } else {
                    verlet.add(Ex1Particle.getNextVerlet(verlet.get(verlet.size() - 2), verlet.getLast(), dt));
                }
            }
            verlet.removeFirst(); // Remove the initial particle that was calculated with inverse euler

            // Save all results for this configuration
            ex1Results.results().add(new ResultsForDt(dt, analytical, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        }

        return ex1Results;
    }


}
