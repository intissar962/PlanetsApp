package com.example.planetsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class UpdatePlanetActivity extends AppCompatActivity {

    private EditText planetNameEditText, planetDistanceEditText, planetDescriptionEditText;
    private int planetId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_planet);
        planetNameEditText = findViewById(R.id.planetName);
        planetDistanceEditText = findViewById(R.id.planetDistance);
        planetDescriptionEditText = findViewById(R.id.planetDescription);

        Intent intent = getIntent();
        Planet planet = (Planet) intent.getSerializableExtra("planet");
        planetId = planet.getId();

        planetNameEditText.setText(planet.getName());
        planetDistanceEditText.setText(planet.getDistance());
        planetDescriptionEditText.setText(planet.getDescription());

        final Button button = findViewById(R.id.button_update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = planetNameEditText.getText().toString().trim();
                final String distance = planetDistanceEditText.getText().toString().trim();
                final String description = planetDescriptionEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    planetNameEditText.setError("Name is required");
                    planetNameEditText.requestFocus();
                    return;
                }

                if (distance.isEmpty()) {
                    planetDistanceEditText.setError("Distance is required");
                    planetDistanceEditText.requestFocus();
                    return;
                }
                if (description.isEmpty()) {
                    planetDescriptionEditText.setError("Description is required");
                    planetDescriptionEditText.requestFocus();
                    return;
                }
                Planet planet1 = new Planet(name, distance, description);
                planet1.setId(planetId);
                updatePlanet(planet1);
                observePlanetList();
            }
        });
    }

    private void updatePlanet(Planet planet) {
        PlanetViewModel planetViewModel = new ViewModelProvider(this).get(PlanetViewModel.class);
        planetViewModel.update(planet);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(AddNewPlanetActivity.EXTRA_ID, planet.getId());
        resultIntent.putExtra(AddNewPlanetActivity.EXTRA_PLANET_NAME, planet.getName());
        resultIntent.putExtra(AddNewPlanetActivity.EXTRA_DISTANCE, planet.getDistance());
        resultIntent.putExtra(AddNewPlanetActivity.EXTRA_DESCRIPTION, planet.getDescription());

        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "La planète à été bien modifier", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void observePlanetList() {
        PlanetViewModel planetViewModel = new ViewModelProvider(this).get(PlanetViewModel.class);
        planetViewModel.getAllPlants().observe(this, new Observer<List<Planet>>() {
            @Override
            public void onChanged(List<Planet> planets) {
            }
        });
    }
}
