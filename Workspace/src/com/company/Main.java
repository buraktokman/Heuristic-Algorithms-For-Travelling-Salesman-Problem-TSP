/* -------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 24
Modified   	: 2021 Jun 18
Author(s)	: Batuhan Aydın (1731278), Velid Nazari (1404663), Ercument Burak Tokman (1315490)
Reference	:
#------------------------------------------------------------------------------- */

package com.company;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //  PROGRAM START TIME
        long initialTime = System.currentTimeMillis();

        /*
         *    LOAD COORDINATES FILE
         */
        // String mainFile = null; //args[0];
        String coordFile = "src/com/company/ca4663_only_coordinates.tsp";       // DEFAULT
        System.out.println("INPUT:\t" + coordFile);

        /*
         *    LOAD TO ARRAY - double [[city1,x1,y1], [city2,x2,y2], ...]
         */
        System.out.println("LOADING COORDINATES...");
        List<double[]> locations = getCoordinates(coordFile);
        // System.out.println(coordinates);
        System.out.println("CITY COUNT:\t\t" + locations.size());

        System.out.println("NOTE:\tCoordinates will be printed without decimals to save space.");


        /*
         *      START - NEAREST NEIGHBOUR
         */
        try {
            System.out.println("\n------------------------------------\nSTARTING: \tNearestNeighbor");
            // START TIMER
            long initTimeNN = System.currentTimeMillis();
            // BEGIN TSP
            NearestNeighbor nearest = new NearestNeighbor();
            nearest.run(locations);
            // COMPLETED IN
            System.out.println("COMPLETED: \tNearestNeighbor");
            System.out.println("DURATION: \t" + String.format("%,d", System.currentTimeMillis() - initTimeNN) + "ms");

        }catch(Exception e){
            System.out.println("\nERROR: Something went wrong - Nearest" + e);
        }


        /*
         *      START - REPETITIVE
         */
        try {
            System.out.println("\n====================================\nSTARTING: \tRepetitiveNearestNeighbor");

            // START TIMER
            long initTimeNN = System.currentTimeMillis();
            // BEGIN TSP
            Repetitive_nn_tsp repetitive = new Repetitive_nn_tsp();
            repetitive.run(locations);
            // COMPLETED IN
            System.out.println("COMPLETED: \tRepetitiveNearestNeighbor");
            System.out.println("DURATION: \t" + String.format("%,d", System.currentTimeMillis() - initTimeNN) + "ms");

        }catch(Exception e){
           System.out.println("\nERROR: Something went wrong - RepetitiveNearest" + e);
        }


        /*
         *      START - GREEDY ALGORITHM
         */
       try {
            System.out.println("\n------------------------------------\nSTARTING: \tGreedy");
            // START TIMER
            long initTimeGreedy = System.currentTimeMillis();
            GreedyAlgo greedy = new GreedyAlgo();
            greedy.run(locations);
            // DURATION
            System.out.println("COMPLETED: \tGreedy");
            System.out.println("DURATION: \t" + String.format("%,d", System.currentTimeMillis() - initTimeGreedy) + "ms");
        }catch(Exception e){
            System.out.println("\nERROR: Something went wrong - Greedy" + e);
        }


        /*
         *    START - DIVIDE AND CONQUER
        */
        try {
            System.out.println("\n------------------------------------\nSTARTING: \tDivide & Conquer");
            // START TIMER
            long initTimeDivide = System.currentTimeMillis();
            DivideAndConquer divideconquer = new DivideAndConquer(locations);
            divideconquer.run();
            // COMPLETED IN
            System.out.println("COMPLETED: \tDivide & Conquer");
            System.out.println("DURATION: \t" + String.format("%,d", System.currentTimeMillis() - initTimeDivide) + "ms");
        }catch(Exception e){
            System.out.println("\nERROR: Something went wrong - D&C" + e);
        }

        // PROGRAM TIME
        System.out.println("\n\nTotal execution time: " + String.format("%,d", System.currentTimeMillis() - initialTime) + "ms");
    }

    // READ FILE TO ARRAY
    public static List<double[]> getCoordinates(String filePath){
        List<double[]> list = new ArrayList<double[]>();
        //ArrayList<double[]> list = new ArrayList<double[]>();
        try{
            // READ
            Path pathToFile = Paths.get(filePath).toAbsolutePath();
            Scanner sc = new Scanner(new File(String.valueOf(pathToFile)));
            while (sc.hasNextLine()){    //hasNext() for word by word
                String line = sc.nextLine();
                // Split to [index, x, y]
                String[] lineElements = line.split("\\s+");
                // Integer.parseInt(lineElements[0])
                double coordinates[] = {Double.valueOf(lineElements[0]), Double.valueOf(lineElements[1]), Double.valueOf(lineElements[2])};
                //System.out.println(coordinates[0] + "\tx:" + coordinates[1] + "\ty:" + coordinates[2]);
                list.add(coordinates);
            }
            sc.close();

        }catch (IOException e){
            System.out.println("Error file read:\n" + e);
        }
        return list;
    }
}
