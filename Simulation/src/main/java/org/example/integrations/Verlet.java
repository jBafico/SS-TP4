package org.example.integrations;


import org.example.Particle;

import java.util.ArrayList;
import java.util.List;

public class Verlet{

    public List<Particle> integrate(List<Particle> particleList,double currentTime, double dt) {
        //List with new state of particles
        List<Particle> newStateParticles = new ArrayList<Particle>();

        for (int i = 0; i < particleList.size(); i++) {
            Particle p= particleList.get(i);
            if(i==0){

            }
            else if(i==particleList.size()-1){

            }
            else{

            }
        }

        return null;
    }
}
