package org.example.ex1;

import org.example.MemoryFriendlyArrayList;

public class Ex1Simulation {
    public Ex1Results run(Ex1SimulationInfo ex1SimulationInfo) {
        MemoryFriendlyArrayList<Ex1Particle> analytical = new MemoryFriendlyArrayList<>(ex1SimulationInfo.saveInterval(), 5);
        MemoryFriendlyArrayList<Ex1Particle> beeman = new MemoryFriendlyArrayList<>(ex1SimulationInfo.saveInterval(), 5);
        MemoryFriendlyArrayList<Ex1Particle> verlet = new MemoryFriendlyArrayList<>(ex1SimulationInfo.saveInterval(), 5);
        MemoryFriendlyArrayList<Ex1Particle> gear5 = new MemoryFriendlyArrayList<>(ex1SimulationInfo.saveInterval(), 5);
        double dt = ex1SimulationInfo.dt();

        // Set up the Ex1Particle class
        Ex1Particle.setParams(ex1SimulationInfo);

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

        for (double t = 0; t <= ex1SimulationInfo.tf(); t += dt) {
            analytical.add(analytical.getLast().createNextAnalytical());
            beeman.add(beeman.getLast().createNextBeeman(beeman.getSecondLast()));
            gear5.add(gear5.getLast().createNextGear());
            verlet.add(verlet.getLast().createNextVerlet(verlet.getSecondLast()));
        }

        // Remove the initial particle that was calculated with inverse euler
        beeman.removeFirst();
        verlet.removeFirst();

        return new Ex1Results(ex1SimulationInfo, new ResultsForDt(dt, analytical.getList(), beeman.getList(), gear5.getList(), verlet.getList()));
    }
}
