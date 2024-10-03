package org.example.ex2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Particle;
import org.example.ex1.Ex1Results;
import org.example.interfaces.Simulation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Ex2Simulation implements Simulation<Ex2Params, Ex2Results> {
    public Ex2Results run(Ex2Params params, String timestamp, String outputFilePath){
//        Ex2Results results = new Ex2Results(params, new ArrayList<>());
        return null;
    }

    private List<Particle> generateEx2Particles(Ex2Params params) {
        List<Particle> generatedParticles = new ArrayList<>();

        // Generate the particles with initial force 0
        double positionIterator = 0;
        for (int i = 0; i < params.N(); i++) {
            // If we are in the last particle, add the armonic force
            boolean hasArmonicForce = i == params.N()-1;
//            generatedParticles.add(new Ex2Particle(positionIterator, 0, hasArmonicForce, params));
            positionIterator += params.l();
        }

        return generatedParticles;
    }

    private void writeOutput(Ex2Results results, String timestamp, String outputFilePath) {
        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        String finalFilePath = String.format("%s/ex2_results_%s.json", outputFilePath, timestamp);
        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(finalFilePath),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )) {
            // Convert the Ex1Results object to JSON string
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);

            // Write the JSON string to the file
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Could not write files.", e);
        }
    }
}
