package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ex1.Ex1Params;
import org.example.ex2.Ex2Params;


public record GlobalParams(
        Ex1Params ex1Params,
        Ex2Params ex2Params
) {}
