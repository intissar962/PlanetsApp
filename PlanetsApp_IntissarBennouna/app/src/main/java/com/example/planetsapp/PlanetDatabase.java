package com.example.planetsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Planet.class}, version = 1)
public abstract class PlanetDatabase extends RoomDatabase {
    public abstract PlanetDAO planetDAO();

    /**
     * To create singleton PlanetDatabase to prevent having multiple instances
     * of the database opened at the same time.
     */
    private static volatile PlanetDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PlanetDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlanetDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PlanetDatabase.class, "planet_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PlanetDAO dao = INSTANCE.planetDAO();
                dao.deleteAll();

                Planet planet = new Planet("Planet 1", "10", "Description planet 1");
                dao.insert(planet);
                planet = new Planet("Planet 2", "20", "Description planet 2");
                dao.insert(planet);
            });
        }
    };

}
