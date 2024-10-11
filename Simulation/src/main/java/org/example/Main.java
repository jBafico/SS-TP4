package org.example;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.ex1.Ex1Results;
import org.example.ex1.Ex1Simulation;
import org.example.ex1.ResultsForDt;
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
import java.util.List;

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
            // Create a JsonGenerator for incremental writing
            JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(writer);
            if ( results instanceof Ex1Results ex1Results){

                // Start writing the JSON object
                jsonGenerator.writeStartObject();

                // Assuming Results has a list or other large fields that you want to write incrementally
                jsonGenerator.writeFieldName("params");  // Replace with actual field names
                jsonGenerator.writeObject(ex1Results.params());  // Replace with the actual method to get the field

                // You can add more fields here and write incrementally
                jsonGenerator.writeRaw(", \"resultsByRepetitionNo\" : { \"0\" : ");
                jsonGenerator.writeRaw("[{");  // Opening array and object
                List<ResultsForDt> resultsList = ex1Results.resultsByRepetitionNo().get(0);
                for (int i = 0; i < resultsList.size() ; i++){
                    var currentResult = resultsList.get(i);
                    jsonGenerator.writeRaw("\"dt\" : ");  // Manually write the first field
                    jsonGenerator.writeRaw(String.valueOf(currentResult.dt));  // Write the value of dt

                    jsonGenerator.writeFieldName("analytical");
                    jsonGenerator.writeObject(currentResult.analytical);
                    jsonGenerator.flush();
                    currentResult.analytical = null;
                    System.gc();
                    jsonGenerator.writeFieldName("beeman");
                    jsonGenerator.writeObject(currentResult.beeman);
                    jsonGenerator.flush();
                    currentResult.beeman = null;
                    System.gc();
                    jsonGenerator.writeFieldName("gear5");
                    jsonGenerator.writeObject(currentResult.gear5);
                    jsonGenerator.flush();
                    currentResult.gear5 = null;
                    System.gc();
                    jsonGenerator.writeFieldName("verlet");
                    jsonGenerator.writeObject(currentResult.verlet);
                    jsonGenerator.flush();
                    currentResult.verlet = null;
                    System.gc();
                    if ( i != resultsList.size() - 1){
                        //si no es el ultimo pongo comma
                        jsonGenerator.writeRaw("},{");
                    } else {
                        jsonGenerator.writeRaw("}]");
                    }

                }

                jsonGenerator.writeEndObject();

                // Finish writing the JSON object
                jsonGenerator.writeRaw("}");

                // Flush and close the JsonGenerator
                jsonGenerator.flush();
                jsonGenerator.close();
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not write files.", e);
        }
    }
}