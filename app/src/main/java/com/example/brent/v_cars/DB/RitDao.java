package com.example.brent.v_cars.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.brent.v_cars.Model.Rit;

import java.util.List;

@Dao
public interface RitDao {
    @Query("Select * From Rit")
    List<Rit> getAll();

    @Query("Select * From Rit Order By naam")
    List<Rit> getAllOrderByNaam();

    @Query("Delete From Rit")
    void deleteAll();

    @Query("Select * From Rit Where rit_id In (:ritId)")
    Rit getById(int ritId);

    @Insert
    void insert(Rit rit);

    @Query("DELETE From Rit Where rit_id In (:ritId)")
    void deleteById(int ritId);

    @Update
    void update(Rit rit);
}
