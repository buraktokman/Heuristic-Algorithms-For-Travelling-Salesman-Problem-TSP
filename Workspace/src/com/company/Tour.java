package com.company;

public class Tour {
    private Integer[] cities;
    private double totalDistance;

    public Tour(Integer[] cities, double totalDistance) {
        this.cities = cities;
        this.totalDistance = totalDistance;
    }

    public Integer[] getCities() {
        return cities;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void print() {
        System.out.println("CITY PATH");
        for (Integer city : cities) {
            System.out.println("City no:"+city);
        }

        System.out.println("Total distance:"+totalDistance);
    }
}
