package com.example.planetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewPlanetActivity extends AppCompatActivity {
    public static final String EXTRA_DESCRIPTION = "com.gtappdevelopers.gfgroomdatabase.EXTRA_DESCRIPTION";
    public static final String EXTRA_DISTANCE = "com.gtappdevelopers.gfgroomdatabase.EXTRA_DESCRIPTION";
    public static final String EXTRA_PLANET_NAME = "com.gtappdevelopers.gfgroomdatabase.EXTRA_DESCRIPTION";
    public static final String EXTRA_ID = "com.gtappdevelopers.gfgroomdatabase.EXTRA_ID";


    private EditText planetName, planetDistance, planetDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_planet);

        planetName = findViewById(R.id.planetName);
        planetDistance = findViewById(R.id.planetDistance);
        planetDescription = findViewById(R.id.planetDescription);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            final String name = planetName.getText().toString().trim();
            final String distance = planetDistance.getText().toString().trim();
            final String description = planetDescription.getText().toString().trim();

            if (name.isEmpty()) {
                planetName.setError("Name is required");
                planetName.requestFocus();
                return;
            }

            if (distance.isEmpty()) {
                planetDistance.setError("Distance is required");
                planetDistance.requestFocus();
                return;
            }
            if (description.isEmpty()) {
                planetDescription.setError("Distance is required");
                planetDescription.requestFocus();
                return;
            }
            Planet planet = new Planet(name, distance, description);
            savePlanet(planet);

            setResult(RESULT_OK, replyIntent);
            finish();
        });
    }

    private void savePlanet(Planet planet) {
        PlanetDatabase planetDatabase = PlanetDatabase.getDatabase(getApplicationContext());
        PlanetDAO planetDAO = planetDatabase.planetDAO();
        PlanetDatabase.databaseWriteExecutor.execute(() -> {
            // Call the insert method of the PlanetDAO to save the planet
            planetDAO.insert(planet);
        });
        Toast.makeText(this, "La planète à été bien ajouter", Toast.LENGTH_SHORT).show();
    }

}