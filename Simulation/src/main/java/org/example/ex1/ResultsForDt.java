package org.example.ex1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ResultsForDt {
    public double dt;
    public List<Ex1Particle> analytical;
    public List<Ex1Particle> beeman;
    public List<Ex1Particle> gear5;
    public List<Ex1Particle> verlet;

}