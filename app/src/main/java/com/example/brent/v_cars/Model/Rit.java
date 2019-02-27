package com.example.brent.v_cars.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Rit {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "rit_id")
    private int id;
    private String naam;
    private double afstandHeenInKm;
    private double Prijs;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public double getAfstandHeenInKm() {
        return afstandHeenInKm;
    }

    public void setAfstandHeenInKm(double afstandHeenInKm) {
        this.afstandHeenInKm = afstandHeenInKm;
    }

    public double getPrijs() {
        return Prijs;
    }

    public void setPrijs(double prijs) {
        this.Prijs = prijs;
    }

    @Override
    public String toString() {
        return getNaam() + " (" + getAfstandHeenInKm() + "km)" + " - € " + getPrijs();
    }
}
