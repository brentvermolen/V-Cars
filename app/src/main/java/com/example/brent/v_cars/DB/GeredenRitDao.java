package com.example.brent.v_cars.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.brent.v_cars.Model.GeredenRit;

import java.util.List;

@Dao
public interface GeredenRitDao {
    @Query("Select * From GeredenRit Order By datum Desc")
    List<GeredenRit> getAllOrderByDate();

    @Insert
    void insert(GeredenRit geredenRit);

    @Query("Delete From GeredenRit")
    void deleteAll();

    @Query("Delete From GeredenRit Where gereden_id=(:id)")
    void deleteById(int id);
}
