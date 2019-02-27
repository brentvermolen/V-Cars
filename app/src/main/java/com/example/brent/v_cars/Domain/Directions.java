package com.example.brent.v_cars.Domain;

import java.util.HashMap;
import java.util.List;

public class Directions {
    private List<List<HashMap<String,String>>> directions;
    private int aantalMeter;

    public Directions(List<List<HashMap<String, String>>> directions, int aantalMeter){
        this.directions = directions;
        this.aantalMeter = aantalMeter;
    }

    public List<List<HashMap<String, String>>> getDirections() {
        return directions;
    }

    public void setDirections(List<List<HashMap<String, String>>> directions) {
        this.directions = directions;
    }

    public int getAantalMeter() {
        return aantalMeter;
    }

    public void setAantalMeter(int aantalMeter) {
        this.aantalMeter = aantalMeter;
    }
}
