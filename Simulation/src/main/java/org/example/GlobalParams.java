package org.example;

import org.example.ex1.Ex1Params;
import org.example.ex2.Ex2Params;


public record GlobalParams(
        Ex1Params ex1Params,
        Ex2Params ex2Params
) {}
