/* -------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 24
Modified   	: 2021 Jun 18
Author(s)	: Batuhan Aydın (1731278)
Reference	:
#------------------------------------------------------------------------------- */

package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Stream;

public class DivideAndConquer {

    public final static String TOUR_FILE = "src/com/company/output_divideconquer.tsp"; // TOUR FilE

    public static final int DIVIDE_BOUNDARY = 3;

    private HashMap<Integer, double[]> citiesMap;

    public DivideAndConquer(List<double[]> cityLocations) {
        this.citiesMap = new HashMap<>();
        for (double[] city : cityLocations) {
            this.citiesMap.put((int)city[0], new double[]{city[1], city[2]});
        }
    }

    public void run(){

        /*
         *   BEGIN - ALGORITHM
         */

        /*
         *   START TRAVELLING
         */
        System.out.println("\n------------ TRAVEL BEGIN ------------");

        Tour shortestTour = divideAndJoin(new ArrayList<>(citiesMap.keySet()));

        shortestTour.print();
        /*
         *   END - ALGORITHM
         */

        // SAVE TO FILE
        saveToFile(shortestTour);

        // REPORT
        System.out.println("\n------------ TRAVEL END ------------\nTOUR COMPLETED!");
        System.out.println("LENGTH:\t" + String.format("%,d", (int)shortestTour.getTotalDistance())); // (int)
        System.out.print("PATH:\t");
        /*for (double[] city: citiesVisited){
            System.out.print((int)city[0] + " > ");
        }*/
        System.out.println("\nTOUR:\tPath saved to output file.");
        //System.out.print("\n"); // ------

    }

    private Tour divideAndJoin(List<Integer> cityList) {
        System.out.println("divide and join...");
        if (cityList.size() <= DIVIDE_BOUNDARY) {
            return generateExhaustiveTour(cityList);
        } else {
            List<List<Integer>> halfCityGroups = splitCities(cityList);

            return joinTours(divideAndJoin(halfCityGroups.get(0)), divideAndJoin(halfCityGroups.get(1)));
        }
    }

    private Tour generateExhaustiveTour(List<Integer> cityList) {
        System.out.println("generate exhaustive for " + cityList.size() + " elements");
        List<Integer[]> cityPermutations = createPermutations(cityList);

        Integer[] selectedPermutation = {};
        double minCalculatedDistance = Double.MAX_VALUE;
        for (Integer[] cityPermutation : cityPermutations) {
            double tourDistance = calculateTourDistance(cityPermutation);

            if (tourDistance < minCalculatedDistance) {
                minCalculatedDistance = tourDistance;
                selectedPermutation = cityPermutation;
            }
        }

        return new Tour(selectedPermutation, minCalculatedDistance);
    }

    private List<Integer[]> createPermutations(List<Integer> cityList) {
        System.out.println("create permutations for " + cityList.size() + " elements");
        List<Integer[]> returnList = new ArrayList<>();
        Integer[] cityArray = new Integer[cityList.size()];
        cityList.toArray(cityArray);

        createAllRecursive(cityList.size(), cityArray, returnList);

        return returnList;
    }

    // REFERENCE: https://www.baeldung.com/java-array-permutations
    private void createAllRecursive(int n, Integer[] elements, List<Integer[]> returnList) {
        System.out.println("create all recursive for " + n);
        if (n == 1) {
            returnList.add(elements.clone());
        } else {
            for (int i = 0; i < n-1; i++) {
                createAllRecursive(n-1, elements, returnList);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                } else {
                    swap(elements, 0, n-1);
                }
            }

            createAllRecursive(n-1, elements, returnList);
        }
    }

    private void swap(Integer[] input, int a, int b) {
        Integer tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private double calculateTourDistance(Integer[] cityPermutation) {
        System.out.println("calculate tour distance...");
        double totalTourDistance = 0.0;
        for (int i=0; i<cityPermutation.length-1; i++) {
            double firstCityX = citiesMap.get(cityPermutation[i])[0];
            double firstCityY = citiesMap.get(cityPermutation[i])[1];
            double secondCityX = citiesMap.get(cityPermutation[i+1])[0];
            double secondCityY = citiesMap.get(cityPermutation[i+1])[1];

            totalTourDistance += getDistance(firstCityX, firstCityY, secondCityX, secondCityY);
        }

        double lastCityX = citiesMap.get(cityPermutation[cityPermutation.length-1])[0];
        double lastCityY = citiesMap.get(cityPermutation[cityPermutation.length-1])[1];
        double firstCityX = citiesMap.get(cityPermutation[0])[0];
        double firstCityY = citiesMap.get(cityPermutation[0])[1];

        totalTourDistance += getDistance(lastCityX, lastCityY, firstCityX, firstCityY);

        return totalTourDistance;
    }

    private List<List<Integer>> splitCities(List<Integer> cityList) {
        System.out.println("split cities for " + cityList.size() + " elements");
        double xDifference = getXDifference(cityList);
        double yDifference = getYDifference(cityList);

        if (xDifference >= yDifference) {
            cityList = sortByX(cityList);
        } else {
            cityList = sortByY(cityList);
        }

        int boundaryIndex = cityList.size() / 2;
        List<Integer> firstHalf = new ArrayList<>(cityList.subList(0, boundaryIndex));
        List<Integer> secondHalf = new ArrayList<>(cityList.subList(boundaryIndex, cityList.size()));

        List<List<Integer>> cityLists = new ArrayList<>();
        cityLists.add(0, firstHalf);
        cityLists.add(1, secondHalf);

        return cityLists;
    }

    private Tour joinTours(Tour tour1, Tour tour2) {
        System.out.println("join tours...");
        List<Segment> segments1 = createSegments(tour1);
        List<Segment> segments2 = createSegments(tour2);

        Tour selectedTour = null;
        for (Segment s1 : segments1) {
            for (Segment s2 : segments2) {
                Tour calculatedTour = joinSegments(s1, s2);

                if (selectedTour == null) {
                    selectedTour = calculatedTour;
                } else if (selectedTour.getTotalDistance() > calculatedTour.getTotalDistance()) {
                    selectedTour = calculatedTour;
                }
            }
        }

        return selectedTour;
    }

    private List<Segment> createSegments(Tour tour) {
        System.out.println("create segments...");
        List<Integer> tourCityList = Arrays.asList(tour.getCities());
        List<Segment> segmentList = new ArrayList<>();

        for (int i=0; i<tourCityList.size(); i++) {
            Collections.rotate(tourCityList, 1);

            Segment s = new Segment((Integer[]) tourCityList.toArray());
            segmentList.add(s);
        }

        return segmentList;
    }

    private Tour joinSegments(Segment segment1, Segment segment2) {
        System.out.println("join segments...");
        Integer[] candidateTour1 = Stream.concat(Arrays.stream(segment1.getCities()), Arrays.stream(segment2.getCities()))
                .toArray(Integer[]::new);

        List<Integer> segment2CityList = Arrays.asList(segment2.getCities());
        Collections.reverse(segment2CityList);
        Integer[] segment2CitiesReversed = (Integer[])segment2CityList.toArray();

        Integer[] candidateTour2 = Stream.concat(Arrays.stream(segment1.getCities()), Arrays.stream(segment2CitiesReversed))
                .toArray(Integer[]::new);

        double candidateTour1Distance = calculateTourDistance(candidateTour1);
        double candidateTour2Distance = calculateTourDistance(candidateTour2);

        if (candidateTour1Distance <= candidateTour2Distance) {
            return new Tour(candidateTour1, candidateTour1Distance);
        } else {
            return new Tour(candidateTour2, candidateTour2Distance);
        }
    }

    private double getXDifference(List<Integer> cityList) {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        for (Integer city : cityList) {
            double cityX = citiesMap.get(city)[0];
            if (cityX < minX) {
                minX = cityX;
            }

            if (cityX > maxX) {
                maxX = cityX;
            }
        }

        return maxX - minX;
    }

    private double getYDifference(List<Integer> cityList) {
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Integer city : cityList) {
            double cityY = citiesMap.get(city)[1];
            if (cityY < minY) {
                minY = cityY;
            }

            if (cityY > maxY) {
                maxY = cityY;
            }
        }

        return maxY - minY;
    }

    private List<Integer> sortByX(List<Integer> cityList) {
        System.out.println("sort by x...");
        List<Integer> sortedList = new ArrayList<>();
        int listSize = cityList.size();

        while (sortedList.size() < listSize) {
            Integer selectedCity = -1;
            double minElement = Double.MAX_VALUE;
            for (Integer city : cityList) {
                if (citiesMap.get(city)[0] < minElement) {
                    minElement = citiesMap.get(city)[0];
                    selectedCity = city;
                }
            }

            sortedList.add(selectedCity);
            cityList.remove(selectedCity);
        }

        return sortedList;
    }

    private List<Integer> sortByY(List<Integer> cityList) {
        System.out.println("sort by y...");
        List<Integer> sortedList = new ArrayList<>();
        int listSize = cityList.size();

        while (sortedList.size() < listSize) {
            Integer selectedCity = -1;
            double minElement = Double.MAX_VALUE;

            for (Integer city : cityList) {
                if (citiesMap.get(city)[1] < minElement) {
                    minElement = citiesMap.get(city)[1];
                    selectedCity = city;
                }
            }

            sortedList.add(selectedCity);
            cityList.remove(selectedCity);
        }

        return sortedList;
    }

    // CALCULATE DISTANCE BETWEEN TWO POINTS
    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    // OUTPUT TO FILE
    private void saveToFile(Tour tour){
        File fileName = new File(TOUR_FILE);
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter output = new BufferedWriter(fw);
            for (Integer city : tour.getCities()) {
                output.write(city + "\t" + citiesMap.get(city)[0] + "\t" + citiesMap.get(city)[1]);
                output.newLine();
            }
            output.close();
        } catch (Exception e) {
            System.out.println("ERROR: Cannot write to file:\n" + e);
        }
    }

}

