package com.example.planetsapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import kotlin.jvm.internal.SerializedIr;


@Entity (tableName = "planet_table")
public class Planet  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "distance")
    private String distance;
    @ColumnInfo(name = "description")
    private String description;

    public Planet(@NonNull String name, String distance, String description) {
        this.name = name;
        this.distance = distance;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName(){return this.name;}
    public void setName(String name) {
        this.name = name;
    }
    public String getDistance(){return this.distance;}
    public void setDistance(String distance) { this.distance = distance; }
    public String getDescription(){return this.description;}
    public void setDescription(String description) { this.description = description; }


}
