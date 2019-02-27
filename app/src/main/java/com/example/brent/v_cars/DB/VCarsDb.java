package com.example.brent.v_cars.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.brent.v_cars.Model.*;

import java.util.concurrent.Executors;

@Database(entities =  {Rit.class, Settings.class, GeredenRit.class }, version = 1)
public abstract class VCarsDb extends RoomDatabase {
    private static VCarsDb INSTANCE;
    private static final String DB_NAME = "vcars.db";

    public abstract RitDao rittenDao();
    public abstract SettingsDao settingsDao();
    public abstract GeredenRitDao geredenRitDao();

    public static VCarsDb getDatabase (final Context context){
        if (INSTANCE == null) {
            synchronized (VCarsDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VCarsDb.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            new EmptyAndFillDbAsync(INSTANCE).insertData();
                                        }
                                    });

                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    /*public void clearDb(){
        if (INSTANCE != null){
            new EmptyAndFillDbAsync(INSTANCE).execute();
        }
    }*/

    private static class EmptyAndFillDbAsync {
        private final RitDao rittenDao;
        private final SettingsDao settingsDao;
        private final GeredenRitDao geredenRitDao;

        public EmptyAndFillDbAsync(VCarsDb instance) {
            rittenDao = instance.rittenDao();
            settingsDao = instance.settingsDao();
            geredenRitDao = instance.geredenRitDao();
        }

        protected Void insertData() {
            rittenDao.deleteAll();
            settingsDao.delete();
            geredenRitDao.deleteAll();

            Settings settings = new Settings();
            settings.setPrePaidTegoed(0);
            settings.setPrijsPerKilometer(0.15);
            settings.setHomeAddress("Consciencestraat 8, 2330 Merksplas");

            settingsDao.insert(settings);

            Rit sien = new Rit();
            sien.setNaam("Thuis - Sien");
            sien.setAfstandHeenInKm(20);
            sien.setPrijs(5);

            rittenDao.insert(sien);

            return null;
        }
    }
}