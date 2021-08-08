/* -------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 24
Modified   	: 2021 Jun 18
Author(s)	: Ercument Burak Tokman (1315490)
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

public class NearestNeighbor extends Main{

    public final static String TOUR_FILE = "src/com/company/output_nearest.tsp";       // TOUR FilE

    public static void run(List<double[]> locations){

        /*
        *   BEGIN - ALGORITHM
        */

        ArrayList<double[]> citiesVisited = new ArrayList<double[]>();                  // CREATE TOUR MATRIX [[cityID, x, y, distance], ...]
        ArrayList<double[]> citiesVisitedUnordered = new ArrayList<double[]>();         // ENDPOINTS        [ [city details], [id,x,y], [id2,x2,y2], ... ]
        double currentCityID = locations.get(0)[0];                                     // STARTING CITY id
        double currentX = locations.get(0)[1];                                          // STARTING CITY x coordinate
        double currentY = locations.get(0)[2];                                          // STARTING CITY y coordinate
        double tourLength = 0;                                                          // TOUR LENGTH
        System.out.println("Start city:" + (int)currentCityID + "\t x:" + (int)currentX + "\t y:" + (int)currentY);

        for (double[] location : locations)                                             // MARK EVERY CITY AS FREE AT THE BEGINNING
            citiesVisitedUnordered.add(location);
        double[] visitedMark = {-1,-1,-1};                                              // VISITED CITY DETAILS WILL BE MARKED
        citiesVisitedUnordered.set(0, visitedMark);                                     // MARK FIRST CITY AS VISITED

        // ADD STARTING CITY
        double startCity[] = {locations.get(0)[0], locations.get(0)[1], locations.get(0)[2], 9999999};
        citiesVisited.add(startCity);

        // START TRAVELLING
        System.out.println("\n------------ TRAVEL BEGIN ------------");
        for (int i = 0; i < locations.size(); i++) {
            //System.out.println("Current city:" + (int)currentCityID + "\t (" + (int)currentX + ", " + (int)currentY + ")\t tourTotal=" + (int)tourLength);

            // RESET / STORE CLOSEST (default is starting city) [id, x, y, distance]
            double closest[] = {-1, -1, -1, 99999999};

            // RETURN TO FIRST CITY IF ALL VISITED
            if (citiesVisited.size() == locations.size()){
                double distanceTemp = getDistance(currentX, currentY, startCity[1], startCity[2]);  // distance between currentCity <-> startingCity
                citiesVisited.add(startCity.clone());                                               // MARK AS VISITED
                tourLength += distanceTemp;                                                         // ADD TO TOUR LENGTH
                System.out.println("currentCity:" + (int)currentCityID + "\t(" + (int)currentX + ", " + (int)currentY + ")\t -> \tnextCity:" + (int)startCity[0] + "\t(" + (int)startCity[1] + ", " + (int)startCity[2] + ")\t dist:" + (int)distanceTemp);
                System.out.println("WARNING: All cities visited. Returning to start.");
                break;
            }

            // CHECK EACH CITY
            //checkCityLoop:
            for (double[] location: locations) {

                // SKIP CITY IF VISITED BEFORE
                if (citiesVisitedUnordered.get((int) location[0] - 1)[0] == -1){
                    //System.out.println("VISITED BEFORE !!\tcity: " + (int) location[0]);
                    continue;
                }

                // CALCULATE DISTANCE TO NEXT CITY
                double distance = getDistance(currentX, currentY, location[1], location[2]);
                // System.out.println("currentCity:" + (int)currentCityID + "\t(" + (int)currentX + ", " + (int)currentY + ")\t -> \tnextCity:" + (int)location[0] + "\t(" + (int)location[1] + ", " + (int)location[2] + ")\t dist:" + (int)distance);

                // SELECT IF CLOSER
                if (closest[3] >= distance && distance != 0){
                    //System.out.println("GETTING CLOSER\t nextCity:" + (int)location[0] + "\t(" + (int)location[1] + ", " + (int)location[2] + ")\t DIST " + (int)distance);
                    // STORE CLOSEST [id, x, y, distance]
                    closest[0] = location[0];   // id
                    closest[1] = location[1];   // x
                    closest[2] = location[2];   // y
                    closest[3] = distance;      // distance
                }
            }

            // MOVE TO NEW CITY
            //System.out.println("FOUND closest city: " + (int)closest[0] + "\t (" + (int)closest[1] + ", " + (int)closest[2] + ")\t dist=" + (int)closest[3]);
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
            System.out.println("MOVED to city:" + (int)currentCityID + "\t (" + (int)currentX + ", " + (int)currentY + ")\t tourTotal=" + (int)tourLength);
        }

        /*
        *   END - ALGORITHM
        */

        // SAVE TO FILE
        saveToFile(citiesVisited);

        // REPORT
        System.out.println("\n------------ TRAVEL END ------------\nTOUR COMPLETED!");
        System.out.println("CITIES:\t" + String.format("%,d", (int)citiesVisited.size()-1)); // (int)
        System.out.println("LENGTH:\t" + String.format("%,d", (int)tourLength)); // (int)
        System.out.print("PATH:\t");
        /*for (double[] city: citiesVisited){
            System.out.print((int)city[0] + " > ");
        }*/
        System.out.println("\nTOUR:\tPath saved to output file.");
        //System.out.print("\n"); // ------

    }


    // CALCULATE DISTANCE BETWEEN TWO POINTS
    public static double getDistance(double x1, double y1, double x2, double y2) {
        //return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
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

