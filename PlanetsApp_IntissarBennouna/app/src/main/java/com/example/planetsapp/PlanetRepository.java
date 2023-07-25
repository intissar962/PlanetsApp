package com.example.planetsapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlanetRepository {

    private PlanetDAO planetDAO;
    private LiveData<List<Planet>> allPlanets;

    PlanetRepository(Application application) {
        PlanetDatabase db = PlanetDatabase.getDatabase(application);
        planetDAO = db.planetDAO();
        allPlanets = planetDAO.getAll();
    }

    LiveData<List<Planet>> getAll() {
        return allPlanets;
    }
    void insert(Planet planet) {
        PlanetDatabase.databaseWriteExecutor.execute(() -> {
            planetDAO.insert(planet);
        });
    }
    void update(Planet planet)
    {
        PlanetDatabase.databaseWriteExecutor.execute(() -> {
            planetDAO.update(planet);
        });
    }

    void delete(Planet planet) {
        new deletePlanetAsyncTask(planetDAO).execute(planet);
    }


    void deleteAll()
    {
        PlanetDatabase.databaseWriteExecutor.execute(() -> {
            planetDAO.deleteAll();
        });
    }
    private static class deletePlanetAsyncTask extends AsyncTask<Planet, Void, Void> {
        private PlanetDAO planetAsyncDAO;

        deletePlanetAsyncTask(PlanetDAO dao) {
            planetAsyncDAO = dao;
        }

        @Override
        protected Void doInBackground(Planet... planets) {
            planetAsyncDAO.delete(planets[0]);
            return null;
        }
    }
    LiveData<List<Planet>> getSearchResults(String searchName)
    {
        return planetDAO.getSearchResults(searchName);
    }



}
