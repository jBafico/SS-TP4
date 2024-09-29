package org.example.integrations;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.Particle;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Verlet{
    // Verlet needs particles(t-dt) and particles(t) to get particles(t+dt)
    private List<Particle> previousParticles= new ArrayList<>();
    private List<Particle> currentParticles= new ArrayList<>();

    public List<Particle> integrate(double currentTime, double dt) {

        //list of the new particles to then convert them to the current state
        List<Particle> newParticles= new ArrayList<>();

        for (int i = 0; i < currentParticles.size(); i++) {
            Particle p= currentParticles.get(i);
            if(i==0){

            }
            else if(i==currentParticles.size()-1){

            }
            else{

            }
        }

        return null;
    }
}
