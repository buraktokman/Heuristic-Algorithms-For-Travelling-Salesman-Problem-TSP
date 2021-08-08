/* -------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 24
Modified   	: 2021 Jun 18
Author(s)	: Velid Nazari (1404663), Ercument Burak Tokman (1315490)
Reference	:
#------------------------------------------------------------------------------- */

package com.company;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Repetitive_nn_tsp extends Main{

    /*
        INCOMPLETE
     */
    public final static String TOUR_FILE = "src/com/company/output_repetitive_nn.tsp";       // TOUR FilE

    public static void run(List<double[]> locations){

        /*
        *   BEGIN - ALGORITHM
        */

        ArrayList<double[]> citiesVisited = new ArrayList<double[]>();                  // CREATE TOUR MATRIX [[cityID, x, y, distance], ...]
        ArrayList<double[]> finalVisited = new ArrayList<double[]>();
        ArrayList<double[]> citiesVisitedUnordered = new ArrayList<double[]>();         // ENDPOINTS        [ [city details], [id,x,y], [id2,x2,y2], ... ]
        double currentCityID = locations.get(0)[0];                                     // STARTING CITY id
        double currentX = locations.get(0)[1];                                          // STARTING CITY x coordinate
        double currentY = locations.get(0)[2];                                          // STARTING CITY y coordinate
        double tourLength = 0;                                                          // TOUR LENGTH
        double finalTourlength = 100000000;
        int finalCitiesVisited = 0;
        
        ArrayList<Integer> numbers = new ArrayList<Integer>();   
        Random randomGenerator = new Random();
        
        while (numbers.size() < locations.size()) {
        	
        	int random = randomGenerator.nextInt(locations.size());
            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }
        System.out.println("\n------------ TRAVEL BEGIN ------------");
        double startCity[] = {locations.get(0)[0], locations.get(0)[1], locations.get(0)[2], 9999999};
        for(int i=0;i<numbers.size();i++){
        	startCity[0] = locations.get(numbers.get(i))[0];
        	startCity[1] = locations.get(numbers.get(i))[1];
        	startCity[2] = locations.get(numbers.get(i))[2];
        	startCity[3] = 9999999;
        	currentCityID = locations.get(numbers.get(i))[0];                                     // STARTING CITY id
            currentX = locations.get(numbers.get(i))[1];                                          // STARTING CITY x coordinate
            currentY = locations.get(numbers.get(i))[2];
            
            for (double[] location : locations)                                             // MARK EVERY CITY AS FREE AT THE BEGINNING
                citiesVisitedUnordered.add(location);
            double[] visitedMark = {-1,-1,-1};                                              // VISITED CITY DETAILS WILL BE MARKED
            citiesVisitedUnordered.set(0, visitedMark);  
        
        citiesVisited.add(startCity);

        // START TRAVELLING
        
        for (int j = 0; j < locations.size(); j++) {

            // RESET / STORE CLOSEST (default is starting city) [id, x, y, distance]
            double closest[] = {-1, -1, -1, 99999999};

            // RETURN TO FIRST CITY IF ALL VISITED
            if (citiesVisited.size() == locations.size()){
                double distanceTemp = getDistance(currentX, currentY, startCity[1], startCity[2]);  // distance between currentCity <-> startingCity
                citiesVisited.add(startCity.clone());                                               // MARK AS VISITED
                tourLength += distanceTemp;                                                         // ADD TO TOUR LENGTH
                break;
            }

            // CHECK EACH CITY
            //checkCityLoop:
            for (double[] location: locations) {

                // SKIP CITY IF VISITED BEFORE
                if (citiesVisitedUnordered.get((int) location[0] - 1)[0] == -1){
                    continue;
                }

                // CALCULATE DISTANCE TO NEXT CITY
                double distance = getDistance(currentX, currentY, location[1], location[2]);

                // SELECT IF CLOSER
                if (closest[3] >= distance && distance != 0){
                    // STORE CLOSEST [id, x, y, distance]
                    closest[0] = location[0];   // id
                    closest[1] = location[1];   // x
                    closest[2] = location[2];   // y
                    closest[3] = distance;      // distance
                }
            }

            // ADD CITY TO VISITED
            double[] cloneClone = closest.clone();
            citiesVisited.add(cloneClone);
            // MARK AS VISITED
            citiesVisitedUnordered.set((int)closest[0] - 1, visitedMark);
            // ADD TO TOUR LENGTH
            tourLength += closest[3];
            // MARK AS CURRENT CITY
            currentCityID = closest[0];
            currentX = closest[1];
            currentY = closest[2];
            //System.out.println("MOVED to city:" + (int)currentCityID + "\t (" + (int)currentX + ", " + (int)currentY + ")\t tourTotal=" + (int)tourLength);
// To hide NN cities output comment line above
        }
        
        finalTourlength = Math.min(tourLength, finalTourlength);
        if(finalTourlength <= tourLength) {
        	finalVisited = (ArrayList<double[]>)citiesVisited.clone();
        }

    	finalCitiesVisited = citiesVisited.size()-1;
    	citiesVisited.clear();
    	citiesVisitedUnordered.clear();
    	tourLength = 0;
        
        }

        /*
        *   END - ALGORITHM
        */
        
        // SAVE TO FILE
        saveToFile(finalVisited);

        // REPORT
        System.out.println("\n------------ TRAVEL END ------------\nTOUR COMPLETED!");
        System.out.println("CITIES:\t" + String.format("%,d", finalCitiesVisited)); // (int)
        System.out.println("LENGTH:\t" + String.format("%,d", (int)finalTourlength)); // (int)
        System.out.print("PATH:\t");
        System.out.println("\nTOUR:\tPath saved to output file.");

    }


    // CALCULATE DISTANCE BETWEEN TWO POINTS
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)); // Point2D.distance(x1, y1, x2, y2);
    }

    // OUTPUT TO FILE
    private static void saveToFile(ArrayList<double[]> tour){
        File fileName = new File(TOUR_FILE);
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            for (int i = 0; i < tour.size(); i++){
                output.write((int)tour.get(i)[0] + "\t" + tour.get(i)[1] + "\t" + tour.get(i)[2]); // line = cityID x y
                output.newLine();
            }
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR: Cannot write to file:\n" + e);
        }
    }

}

