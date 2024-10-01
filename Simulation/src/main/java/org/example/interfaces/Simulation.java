package org.example.interfaces;

public interface Simulation<P extends Params, R extends Results> {
    public R run(P params, String timestamp, String outputFilePath);
}
