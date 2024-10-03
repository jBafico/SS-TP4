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
                if (t == 0) {
                    // Add the initial particle
                    analytical.add(new Ex1Particle(dt));
                    beeman.add(new Ex1Particle(dt));
                    gear5.add(new Ex1Particle(dt));
                    verlet.add(new Ex1Particle(dt));

                    // Use inverse euler to approximate a particle before the initial particle
                    Ex1Particle previousBeeman = beeman.getFirst().createEulerPreviousParticle();
                    Ex1Particle previousVerlet = verlet.getFirst().createEulerPreviousParticle();

                    // Add the approximated particle before the initial particle
                    // This is because Beeman and Verlet need two previous particles to calculate the next one
                    beeman.addFirst(previousBeeman);
                    verlet.addFirst(previousVerlet);
                } else {
                    analytical.add(analytical.getLast().createNextAnalytical());
                    beeman.add(beeman.getLast().createNextBeeman(beeman.get(beeman.size() - 2)));
                    gear5.add(gear5.getLast().createNextGear());
                    verlet.add(verlet.getLast().createNextVerlet(verlet.get(verlet.size() - 2)));
                }

            }

            // Remove the initial particle that was calculated with inverse euler
            beeman.removeFirst();
            verlet.removeFirst();


            // Save all results for this configuration
            ex1Results.results().add(new ResultsForDt(dt, analytical, beeman, gear5, verlet));
        }

        return ex1Results;
    }


}
