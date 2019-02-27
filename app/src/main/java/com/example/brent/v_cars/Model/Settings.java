package com.example.brent.v_cars.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Settings {
    @PrimaryKey
    @ColumnInfo(name = "setting_id")
    private int id;
    private double PrePaidTegoed;
    private double PrijsPerKilometer;
    private String HomeAddress;

    public void setId(int id) {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public double getPrePaidTegoed() {
        return PrePaidTegoed;
    }

    public void setPrePaidTegoed(double prePaidTegoed) {
        PrePaidTegoed = prePaidTegoed;
    }

    public double getPrijsPerKilometer() {
        return PrijsPerKilometer;
    }

    public void setPrijsPerKilometer(double prijsPerKilometer) {
        PrijsPerKilometer = prijsPerKilometer;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }
}
