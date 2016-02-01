package com.example.tristan.thunt_fueltrack;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tristan on 2016-01-12.
 */
public class Entry {
    // So the attributes are
    private String date; // lol
    private String station;
    private float odometer;
    private String fuel_grade;
    private float fuel_amount;
    private float unit_cost;

    public Entry(String station, float odometer, String fuel_grade, float fuel_amount, float unit_cost, String date) {
        this.station = station;
        this.odometer = odometer;
        this.fuel_grade = fuel_grade;
        this.fuel_amount = fuel_amount;
        this.unit_cost = unit_cost;
        // kay we need to fix this date thing too.
        this.date = date;
    }

    public String toString(){
        // compute the fuel cost:
        double fuel_cost = this.unit_cost * this.fuel_amount;

        //returns a String representing the Entry object
        String entryString = "Station: " + this.station + " Fuel Grade: " + this.fuel_grade + " Odometer Reading: " + this.odometer +
                " Fuel amount: " + this.fuel_amount + " Unit Cost: " + this.unit_cost + " Fuel Cost: " + this.getFuel_cost() + " Date: " +date ;
        return entryString;
    }

    // Okay here are all the getters and setters... exciting stuff
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public float getOdometer() {
        return odometer;
    }

    public void setOdometer(float odometer) {
        this.odometer = odometer;
    }

    public String getFuel_grade() {
        return fuel_grade;
    }

    public void setFuel_grade(String fuel_grade) {
        this.fuel_grade = fuel_grade;
    }

    public float getFuel_amount() {
        return fuel_amount;
    }

    public void setFuel_amount(float fuel_amount) {
        this.fuel_amount = fuel_amount;
    }

    public float getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(float unit_cost) {
        this.unit_cost = unit_cost;
    }

    public float getFuel_cost(){
        float fuel_cost = this.unit_cost * this.fuel_amount;
        return fuel_cost;
    }
}
