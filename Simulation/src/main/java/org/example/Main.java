package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ex1.Ex1Results;
import org.example.ex1.Ex1Simulation;
import org.example.ex2.Ex2Results;
import org.example.ex2.Ex2Simulation;
import org.example.interfaces.Results;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

        if (params.ex1Params() != null) {
            System.out.println("Running ex1 simulation...");
            Ex1Simulation ex1Simulation = new Ex1Simulation();
            Ex1Results ex1Results = ex1Simulation.run(params.ex1Params(), timestamp, "./outputs");
            System.out.println("Ex1 simulation finished!");

            System.out.println("Writing ex1 output...");
            writeOutput(ex1Results, timestamp, "./outputs", 1);
            System.out.println("Ex1 output written!");

        }

        if (params.ex2Params() != null) {
            System.out.println("Running ex2 simulation...");
            Ex2Simulation ex2Simulation = new Ex2Simulation();
            Ex2Results ex2Results = ex2Simulation.run(params.ex2Params(), timestamp, "./outputs");
            System.out.println("Ex2 simulation finished!");

            System.out.println("Writing ex2 output...");
            writeOutput(ex2Results, timestamp, "./outputs", 2);
            System.out.println("Ex2 output written!");
        }


        System.out.println("Simulation finished!");
    }

    private static void writeOutput(Results results, String timestamp, String outputFilePath, int exNumber) {
        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        String finalFilePath = String.format("%s/ex%d_results_%s.json", outputFilePath, exNumber, timestamp);
        try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(finalFilePath),
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )) {
            // Convert the Ex1Results object to JSON string
            String jsonString = mapper.writeValueAsString(results);

            // Write the JSON string to the file
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Could not write files.", e);
        }
    }
}