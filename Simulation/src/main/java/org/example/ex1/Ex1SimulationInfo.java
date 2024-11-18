package org.example.ex1;

public record Ex1SimulationInfo(
    double k,
    double y,
    double m,
    double r0,
    double A,
    double dt,
    int saveInterval,
    double tf,
    double repetitionNo
) {
}
