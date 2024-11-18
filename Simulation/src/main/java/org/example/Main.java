package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ex1.*;
import org.example.ex2.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting simulation!");

        GlobalParams params = null;
        try {
            // Load the JSON file from resources
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("./GlobalParams.json");

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the JSON and map it to the Params class
            params = objectMapper.readValue(inputStream, GlobalParams.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Get the current timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Run ex1 simulations
        if (params.ex1Params() != null) {
            System.out.println("STARTED EX1 SIMULATIONS ---------------------");
            // Iterate through the repetitions
            for (int repetitionNo = 0; repetitionNo < params.ex1Params().repetitions(); repetitionNo++){
                final int finalRepetitionNo = repetitionNo;
                final Ex1Params p = params.ex1Params();
                // Iterate through the time steps
                p.dts().forEach(dt -> {
                    final Ex1SimulationInfo ex1SimulationInfo = new Ex1SimulationInfo(p.k(), p.y(), p.m(), p.r0(), p.A(), dt, p.saveInterval(), p.tf(), finalRepetitionNo);
                    System.out.println("Running ex1 simulation with dt: " + dt + " and repetition: " + finalRepetitionNo);
                    Ex1Simulation ex1Simulation = new Ex1Simulation();
                    // Run the simulation
                    Ex1Results ex1Results = ex1Simulation.run(ex1SimulationInfo);
                    // Write the output
                    writeOutput(ex1Results, "./outputs/ex1_results/" + timestamp, "repetition_" + finalRepetitionNo + "_dt_" + dt + ".json");
                });
            }
        }

        // Run ex2 simulations
        if (params.ex2Params() != null) {
            System.out.println("STARTED EX2 SIMULATIONS ---------------------");
            final Ex2Params p = params.ex2Params();
            p.kValues().forEach(k -> {
                final double finalK = k;
                // Iterate through the w values
                for (double w = p.minW(); w <= p.maxW(); w += p.increaseW()) {
                    final Ex2SimulationInfo ex2SimulationInfo = new Ex2SimulationInfo(p.N(), p.m(), p.A(), p.l(), finalK, w, p.tf(), p.saveInterval());
                    System.out.println("Running ex2 simulation with k: " + finalK + " and w: " + w);
                    Ex2Simulation ex2Simulation = new Ex2Simulation();
                    // Run the simulation
                    Ex2Results ex2Results = ex2Simulation.run(ex2SimulationInfo);
                    // Write the output
                    writeOutput(ex2Results, "./outputs/ex2_results/" + timestamp, "k_" + finalK + "_w_" + w + ".json");
                }
            });
            System.out.println("FINISHED EX2 SIMULATIONS ---------------------");
        }
    }


    public static void writeOutput(Object results, String outputDirectoryPath, String filename) {
        try {
            // Create the output directory if it doesn't exist
            Files.createDirectories(Path.of(outputDirectoryPath));

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Define the output file with a unique name for each simulation result
            File outputFile = new File(outputDirectoryPath, filename);

            // Write the SimulationResults to the file in JSON format
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, results);

            System.out.printf("Results successfully written to %s\n", outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}