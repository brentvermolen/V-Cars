package com.example.brent.v_cars.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.brent.v_cars.Model.Settings;

@Dao
public interface SettingsDao {
    @Query("Select * From Settings Where setting_id=0")
    Settings getSettings();

    @Insert
    void insert(Settings settings);

    @Update
    void update(Settings settings);

    @Query("Delete From Settings")
    void delete();
}