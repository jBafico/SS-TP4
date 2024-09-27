package org.example.integrations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.Pair;
import org.example.Particle;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class Gear{

    //Gear needs particles(t) to get particles(t+dt)
    private List<Particle> currentParticles= new ArrayList<Particle>();

    public List<Particle> integrate(double currentTime, double dt) {
        return null;
    }
}
