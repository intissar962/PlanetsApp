package com.example.planetsapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlanetViewModel extends AndroidViewModel {

    private PlanetRepository planetRepository;
    private final LiveData<List<Planet>> allPlants;
    public PlanetViewModel(@NonNull Application application) {
        super(application);
        planetRepository = new PlanetRepository(application);
        allPlants = planetRepository.getAll();
    }
    LiveData<List<Planet>> getAllPlants() {

        return allPlants;
    }
    public void insert(Planet planet)
    {
        planetRepository.insert(planet);
    }
    public void update(Planet planet)
    {
        planetRepository.update(planet);
    }
    public void delete(Planet planet)
    {
        planetRepository.delete(planet);
    }
    public void deleteAll()
    {
        planetRepository.deleteAll();
    }

    public LiveData<List<Planet>> searchPlanetsByName(String planetName) {
        return planetRepository.getSearchResults(planetName);
    }

}
