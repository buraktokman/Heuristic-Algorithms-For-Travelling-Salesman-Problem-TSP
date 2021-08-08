/* -------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 24
Modified   	: 2021 Jun 18
Author(s)	: Ercument Burak Tokman (1315490)
Reference	:
#------------------------------------------------------------------------------- */

package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class GreedyAlgo extends Main{

    public final static String TOUR_FILE = "src/com/company/output_greedy.tsp"; // TOUR FilE

    public static void run(List<double[]> locations){

        /*
         *   BEGIN - ALGORITHM
         */

        // CREATE CITY LINKS - [ [ [id1,x1,y1], [id2,x2,y2], [distance] ], [ [city2],[city3],[distance] ], ... ]
        System.out.println("Constructing city links...");
        ArrayList<double[][]> cityLinksTemp = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            for (int j = 0; j < locations.size(); j++) {
                if (j > i){
                    double distance = getDistance(locations.get(i)[1],locations.get(i)[2],
                                                    locations.get(j)[1],locations.get(j)[2]);                       // dist(x1, y1, x1, y2)
                    double[][] tempLink = { {locations.get(i)[0],locations.get(i)[1],locations.get(i)[2]},          // [  [cityID1,x1,y1],
                                            {locations.get(j)[0],locations.get(j)[1],locations.get(j)[2]},          //    [cityID2,x2,2y2],
                                            {distance} };                                                           //    [distance] ]
                    cityLinksTemp.add(tempLink.clone());                                                            // ADD LINK TO LIST
                }
            }
        }
        // for (double[][] link: cityLinksTemp)
        // System.out.println(link[0][0] + " <--> " + link[1][0] + " dist=" + link[2][0]);


        // ORDER LINKS BY DISTANCE in ASCENDING - (APPLY QUICK SORT)
        System.out.println("Ordering links with Quicksort by distance in ASCENDING...");
        ArrayList<double[][]> cityLinksOrdered = (ArrayList<double[][]>) cityLinksTemp.clone();
        quickSort(cityLinksOrdered, 0, cityLinksOrdered.size()-1);
        //for (double[][] link: cityLinksOrdered)
        // System.out.println((int)link[0][0] + " <--> " + (int)link[1][0] + " dist=" + link[2][0]);
        System.out.println("LINK COUNT:\t" + cityLinksOrdered.size());


        /*
            START TRAVELLING
         */
        System.out.println("\n------------ TRAVEL BEGIN ------------");
        // MEMORY
        double tourLength = 0;                                                  // TOUR LENGTH
        ArrayList<double[]> endpoints = new ArrayList<double[]>();              // ENDPOINTS        [ [city details], [id,x,y], [id2,x2,y2], ... ]
        ArrayList<double[][]> segments = new ArrayList<>();                     // CITY LINKS ADDED [ [cityID1,x1,y1], [cityID2,x2,y2], [cityID3,x3,y3], ... ]
        ArrayList<double[]> segmentsGrouped = new ArrayList<double[]>();        // SEGMENT GROUP    [ [cities in segment], [id1,id7,id3], [id5,id9], ... ]

        for (double[] location : locations) {                                   // MARK EVERY CITY AS FREE ENDPOINT
            endpoints.add(location);
        }

        // ITERATE OVER EACH SEGMENTS & ADD IF SUITABLE
        segmentsLoop:
        for (double[][] newLink : cityLinksOrdered) {                           // cityLinksOrdered = [ [[cityID1,x1,y1], [cityID2,x2,y2], distance], ... ]
            double linkCity1 = newLink[0][0];                                   // Link's start city
            double linkCity2 = newLink[1][0];                                   // Link's end city
            double linkLength = newLink[2][0];                                  // Link's distance

            //System.out.println("------------\nNEW LINK:\t" + (int) linkCity1 + " \t<->\t " + (int) linkCity2 + " \tdist: " + (int) linkLength);

            // CHECK IF LINK CAN BE CONNECTED TO FREE ENDPOINTS
            // System.out.println("CHECKING IF LINK CAN BE CONNECTED TO FREE ENDPOINTS...");
            boolean isEndpointCity1 = false;
            boolean isEndpointCity2 = false;
            if (endpoints.get((int) linkCity1 - 1)[0] == linkCity1)             // CHECK IF 1st CITY IN LINK IS FREE
                isEndpointCity1 = true;
            if (endpoints.get((int) linkCity2 - 1)[0] == linkCity2)             // CHECK IF 2nd CITY IN LINK IS FREE
                isEndpointCity2 = true;
            if (!isEndpointCity1 || !isEndpointCity2) {                         // SKIP LINK IF NO FREE ENDPOINT
                //System.out.println("SKIPPED:\tLink has no free endpoint city");
                continue;
            }

            // CHECK IF LINK CREATES CIRCLE
            //System.out.println("CHECK IF LINK CREATES CIRCLE...");
            segmentGroupLoop:
            for (double[] segment : segmentsGrouped) {                          // [ [cities in segment], [id1,id7,id3], [id5,id9], ... ]
                boolean isCircle1 = false;
                boolean isCircle2 = false;
                // CHECK EACH CITY OF THE SEGMENT
                for (double city : segment) {                                   // [id1,id7,id3]
                    if (city == -1) continue segmentGroupLoop;                  // SKIP IF EMPTY SEGMENT GROUP
                    if (linkCity1 == city) isCircle1 = true;                    // MARK CITY 1
                    if (linkCity2 == city) isCircle2 = true;                    // MARK CITY 2
                    // ALLOW CYCLE IF SEGMENT CONTAINS EVERY CITY               // meaning this is the last link to complete TSP
                    if (segment.length >= locations.size()) {
                        System.out.println("CAUTION:\tLink creating cycle but every city visited!");
                        break segmentGroupLoop;
                    } else if (isCircle1 && isCircle2) {
                        // System.out.println("SKIPPED:\tLink creating cycle\n");
                        continue segmentsLoop;
                    }
                }
            }


            // ADD LINK TO SEGMENTS
            segments.add(newLink.clone());                                      // newLink = [[cityID1,x1,y1], [cityID2,x2,y2], distance]
            tourLength += newLink[2][0];                                        // ADD TO TOTAL TOUR LENGTH
            System.out.println("LINK ADDED:\t" + (int) linkCity1 + " \t<->\t " + (int) linkCity2 + " \tdist: " + (int) linkLength);


            //
            // MANAGE THE SEGMENTS                                              // Link creates new segment or join segments
            //
            // System.out.println("MANAGING SEGMENTS...");
            int segmentIndex1 = -1;
            int segmentIndex2 = -1;
            double[] unfreeMark = {-1, -1, -1};                                 // MARK AS -1 FOR VISITED
            boolean segmentFound = false;
            boolean addedToSegment = false;                                     // 'true' = added to a segment -> 1 endpoint is closed
            // ITERATE OVER EACH SEGMENT GROUP
            for (int r = 0; r < segmentsGrouped.size(); r++) {                  // [ [cities in segment], [id1,id7,id3], [id5,id9], ... ]
                // CHECK EACH CITY
                for (double city : segmentsGrouped.get(r)) {                    // [id1,id7,id3]

                    // ADD NEW LINK TO SEGMENT GROUP
                    double newCityID = -1;
                    if (linkCity1 == city) {
                        newCityID = linkCity2;
                        segmentIndex1 = r;                                      // MARK SEGMENT INDEXES FOR MERGE
                        endpoints.set((int)city - 1, unfreeMark); }             // MARK ENDPOINT AS USED
                    if (linkCity2 == city) {
                        newCityID = linkCity1;
                        segmentIndex2 = r;                                      // MARK INTERMEDIARY CITY AS NOT FREE ENDPOINT
                        endpoints.set((int)city - 1, unfreeMark); }             // MARK ENDPOINT AS USED

                    // SEGMENT GROUP FOUND
                    if (newCityID != -1) { // linkCity1 == city || linkCity2 == city
                        segmentFound = true;
                        double[] pointsTemp = {newCityID};
                        int aLen = segmentsGrouped.get(r).length;
                        int bLen = pointsTemp.length;
                        double[] result = new double[aLen + bLen];
                        System.arraycopy(segmentsGrouped.get(r), 0, result, 0, aLen);
                        System.arraycopy(pointsTemp, 0, result, aLen, bLen);
                        // ADD CITIES IN LINK TO SEGMENT
                        segmentsGrouped.set(r, result.clone());                 // [id,x,y]
                        //System.out.println("SEGMENT:\tLink added to group [" + r + "]");
                    }
                }
            }

            // JOIN SEGMENTS
            boolean segmentsMerged = false;                                     // 'true' = segments merged -> 2 endpoints are closed
            if (segmentIndex1 != -1 && segmentIndex2 != -1) {                   // if both index not -1 meaning an endpoint added to 2 segments
                // ADD CITIES IN BOTH SEGMENTS TO NEW ARRAY
                int aLen = segmentsGrouped.get(segmentIndex1).length;
                int bLen = segmentsGrouped.get(segmentIndex2).length;
                double[] result = new double[aLen + bLen];
                System.arraycopy(segmentsGrouped.get(segmentIndex1), 0, result, 0, aLen);
                System.arraycopy(segmentsGrouped.get(segmentIndex2), 0, result, aLen, bLen);

                // REMOVE DUPLICATE CITIES
                result = Arrays.stream(result).distinct().toArray();

                // ADD CITIES IN LINK TO SEGMENT
                segmentsGrouped.set(segmentIndex1, result.clone());             // [id,x,y]

                // MARK OTHER AS BLANK
                double[] temp = {-1};
                segmentsMerged = true;
                segmentsGrouped.set(segmentIndex2, temp);                       // [id,x,y]

                //System.out.println("SEGMENTS:\tMERGED");
                /*for (double city: result) System.out.print((int)city + " -> ");
                    System.out.println("\n\n");*/
            }
            // CREATE NEW SEGMENT
            if (!segmentFound) {                                                // 'true' means new segment created and no need to check endpoints
                double[] newSegment = {linkCity1, linkCity2};
                segmentsGrouped.add(newSegment.clone());                        // [ [city ids], [id1,id2] ]
                //System.out.println("SEGMENTS:\tNEW CREATED");
            }


            //
            // REMOVE INTERMEDIARY CITIES  (duplicate city ID meaning intermediary city)
            //
            // System.out.println("REMOVING INT. CITIES...");
            // IF A LINK MERGED 2 SEGMENTS THEN BOTH ENDPOINT ARE NOT FREE ANYMORE
            if (segmentsMerged) {
                endpoints.set((int) linkCity1 - 1, unfreeMark);                  // MARK ENDPOINT AS USED
                endpoints.set((int) linkCity2 - 1, unfreeMark);                  // MARK ENDPOINT AS USED
            }
            //System.out.println("ENDPOINTS:\tIntermediary cities removed\n");

            // SHOW FREE ENDPOINTS
            /*System.out.print("FREE ENDPOINTS:\t");
            for (double[] endpoint: endpoints)
                if(endpoint[0] != -1) System.out.print((int)endpoint[0] + " - ");*/

            // STOP IF TOUR COMPLETED
            if (segments.size() == locations.size()) {
                System.out.print("------------------------------------\nSTOP:\t All cities travelled");
                break;
            }
        }

        System.out.println("\n--------------- PATH ---------------");
        // SHOW SELECTED LINKS
        /*for (double[][] segment : segments)
            System.out.println("LINK: \t" + (int) segment[0][0] + " <-> " + (int) segment[1][0]);*/
        // SHOW FREE ENDPOINTS
        System.out.print("\nFREE ENDPOINTS:\t");
        /*for (double[] endpoint: endpoints)
            if (endpoint[0] != -1) System.out.print((int)endpoint[0] + " - ");*/
        System.out.println("\nLINK COUNT: \t" + segments.size() + "\nCITY COUNT: \t" + locations.size());

        /*
            IMPROVE TOUR, REVERSE SEGMENTS - INCOMPLETE
         */



        /*
         *   END - ALGORITHM
         */

        // SAVE TO FILE
        saveToFile(segments);

        // REPORT
        System.out.println("\n------------ TRAVEL END ------------\nTOUR COMPLETED!");
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
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    // OUTPUT TO FILE
    private static void saveToFile(ArrayList<double[][]> segments){
        File fileName = new File(TOUR_FILE);
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            for (double[][] link : segments) {                                  // [ [cityID1,x1,y1], [cityID2,x2,y2], [cityID3,x3,y3], ... ]
                // CAUTION - citys' in line1 <-> line2 is connected but line2 <-> line2 NOT connected!
                // cityID1   x1  y1  <-> cityID2    x2  y2
                output.write((int)link[0][0] + "\t" + link[0][1] + "\t" + link[0][2] + "\t" + (int)link[1][0] + "\t" + link[1][1] + "\t" + link[1][2]);
                output.newLine();
            }
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR: Cannot write to file:\n" + e);
        }
    }


    // QUICK SORT - partition (place pivot element to its place)
    private static int partition(ArrayList<double[][]> cityLinks, int begin, int end) {  // [ [[id1,x1,y1], [id2,x2,2y2], distance], ... ]
        double pivot = cityLinks.get(end)[2][0];                            // PIVOT
        int i = (begin - 1);
        for (int j = begin; j < end; j++) {                                 // Index of smaller elements
            if (cityLinks.get(j)[2][0] <= pivot) {                          // Current element smaller
                i++;                                                        // Increase small element index
                double[][] swapTemp = cityLinks.get(i);
                cityLinks.set(i, cityLinks.get(j).clone());
                cityLinks.set(j, swapTemp.clone());
            }
        }
        double[][] swapTemp = cityLinks.get(i + 1);
        cityLinks.set(i + 1, cityLinks.get(end).clone());
        cityLinks.set(end, swapTemp.clone());
        return i + 1;
    }

    // QUICKSORT - apply
    public static void quickSort(ArrayList<double[][]>  cityLinks, int begin, int end) { // array, start index, end index
        if (begin < end) {
            int partitionIndex = partition(cityLinks, begin, end);                // Partition index
            quickSort(cityLinks, begin, partitionIndex-1);                   // Before partition
            quickSort(cityLinks, partitionIndex+1, end);                    // After partition
        }
    }

}

