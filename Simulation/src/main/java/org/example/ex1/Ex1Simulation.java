package org.example.ex1;

import org.example.interfaces.Simulation;

import java.util.ArrayList;

public class Ex1Simulation implements Simulation<Ex1Params, Ex1Results> {
    @Override
    public Ex1Results run(Ex1Params params, String timestamp, String outputFilePath) {
        Ex1Results results = new Ex1Results(params, new ArrayList<>());

        return results;
    }
}
