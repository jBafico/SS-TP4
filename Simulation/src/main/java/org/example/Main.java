package org.example;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ex1.*;
import org.example.ex2.Ex2Particle;
import org.example.ex2.Ex2Results;
import org.example.ex2.Ex2Simulation;
import org.example.interfaces.Results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.example.Particle.params;

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

//        if (params.ex2Params() != null) {
//            System.out.println("Running ex2 simulation...");
//            Ex2Simulation ex2Simulation = new Ex2Simulation();
//            Ex2Results ex2Results = ex2Simulation.run(params.ex2Params(), timestamp, "./outputs");
//            System.out.println("Ex2 simulation finished!");
//
//            System.out.println("Writing ex2 output...");
//            writeOutput(ex2Results, timestamp, "./outputs", 2);
//            System.out.println("Ex2 output written!");
//        }


        System.out.println("Simulation finished!");
    }


    private static void ex2Serializer(BufferedWriter writer, Ex2Results results) throws IOException {
        Map<Double, Map<Double, List<List<Ex2Particle>>>> resultsByKAndW = results.resultsByKAndW();
        ObjectMapper mapper = new ObjectMapper();  // No pretty printing

        writer.write("{");
        writer.write("\"params\":");
        writer.write(mapper.writeValueAsString(results.params()));  // No pretty printing here
        writer.write(",");
        writer.write("\"resultsByKAndW\": {");
        resultsByKAndW.forEach((k, resultsByW) -> {
            try {
                writer.write(String.format("\"%.2f\": {", k));
                resultsByW.forEach((w, resultsForKAndW) -> {
                    try {
                        System.out.println("Writing results for k: " + k + " and w: " + w);
                        writer.write(String.format("\"%.2f\": [", w));
                        for (List<Ex2Particle> particleList : resultsForKAndW) {
                            String particleListString = mapper.writeValueAsString(particleList);  // No pretty printing
                            writer.write(particleListString);
                            if (resultsForKAndW.indexOf(particleList) != resultsForKAndW.size() - 1) {
                                writer.write(",");
                            }
                        }
                        if (resultsByW.get(w) != resultsByW.values().toArray()[resultsByW.size() - 1]) {
                            writer.write("],");
                        } else {
                            writer.write("]");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Could not write files.", e);
                    }
                });
                if (resultsByKAndW.get(k) != resultsByKAndW.values().toArray()[resultsByKAndW.size() - 1]) {
                    writer.write("},");
                } else {
                    writer.write("}");
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not write files.", e);
            }
        });
        writer.write("}");
        writer.write("}");
    }


//    private static void writeOutput(Results results, String timestamp, String outputFilePath, int exNumber) {
//        // Create an ObjectMapper instance
//        ObjectMapper mapper = new ObjectMapper();
//
//        String finalFilePath = String.format("%s/ex%d_results_%s.json", outputFilePath, exNumber, timestamp);
//        try (BufferedWriter writer = Files.newBufferedWriter(
//                Paths.get(finalFilePath),
//                StandardOpenOption.WRITE,
//                StandardOpenOption.CREATE,
//                StandardOpenOption.TRUNCATE_EXISTING
//        )) {
//            // Create a JsonGenerator for incremental writing
//            JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(writer);
//            if (results instanceof Ex1Results ex1Results) {
//                ex1Serializer(jsonGenerator,ex1Results);
//                return;
//            }
//            ex2Serializer(writer,(Ex2Results) results);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException("Could not write files.", e);
//        }
//    }

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