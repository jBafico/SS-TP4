package org.example.integrations;

import org.example.Pair;
import org.example.Particle;
import org.example.Position;

import java.util.List;

public interface IntegrationService {

    public List<Pair> integrate(List<Particle> particleList);
}
