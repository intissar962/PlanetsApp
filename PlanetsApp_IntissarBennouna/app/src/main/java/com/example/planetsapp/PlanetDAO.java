package com.example.planetsapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface PlanetDAO {

    @Insert
    void insert(Planet planet);

    @Query("DELETE FROM planet_table")
    void deleteAll();

    @Query("SELECT * FROM planet_table")
    LiveData<List<Planet>> getAll();

    @Delete
    void delete(Planet planet);

    @Update
    void update(Planet planet);

    @Query("SELECT * FROM planet_table WHERE name LIKE '%' || :searchName || '%'")
    LiveData<List<Planet>> getSearchResults(String searchName);


}
