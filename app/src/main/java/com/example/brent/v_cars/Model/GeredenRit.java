package com.example.brent.v_cars.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.brent.v_cars.Domain.TimeStampConverter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
public class GeredenRit implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "gereden_id")
    private int id;
    @NonNull
    @TypeConverters(TimeStampConverter.class)
    private Date datum;
    private int bestaandeRit_id;
    private double prijsPerKmOpMomentVanRit;
    private String start;
    private String eind;
    private String startVolledig;
    private String eindVolledig;
    private double aantalKm;

    public GeredenRit(){
        setDatum(Calendar.getInstance().getTime());
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public Date getDatum() {
        return datum;
    }

    public void setDatum(@NonNull Date datum) {
        this.datum = datum;
    }

    public int getBestaandeRit_id() {
        return bestaandeRit_id;
    }

    public void setBestaandeRit_id(int bestaandeRit_id) {
        this.bestaandeRit_id = bestaandeRit_id;
    }

    public double getPrijsPerKmOpMomentVanRit() {
        return prijsPerKmOpMomentVanRit;
    }

    public void setPrijsPerKmOpMomentVanRit(double prijsPerKmOpMomentVanRit) {
        this.prijsPerKmOpMomentVanRit = prijsPerKmOpMomentVanRit;
    }

    @NonNull
    public String getStart() {
        return start;
    }

    public void setStart(@NonNull String start) {
        this.start = start;
    }

    @NonNull
    public String getEind() {
        return eind;
    }

    public void setEind(@NonNull String eind) {
        this.eind = eind;
    }

    public String getStartVolledig() {
        return startVolledig;
    }

    public void setStartVolledig(String startVolledig) {
        this.startVolledig = startVolledig;
    }

    public String getEindVolledig() {
        return eindVolledig;
    }

    public void setEindVolledig(String eindVolledig) {
        this.eindVolledig = eindVolledig;
    }

    @NonNull
    public double getAantalKm() {
        return aantalKm;
    }

    public void setAantalKm(@NonNull double aantalKm) {
        this.aantalKm = aantalKm;
    }
}
