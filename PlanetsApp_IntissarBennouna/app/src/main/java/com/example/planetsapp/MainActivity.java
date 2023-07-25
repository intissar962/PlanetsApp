package com.example.planetsapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planetsapp.R;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private PlanetViewModel planetViewModel;
    private PlanetListAdapter adapter;
    private int selectedPosition;
    private ActivityResultLauncher<Intent> updatePlanetLauncher;
    public static final int NEW_PLANET_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_PLANET_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new PlanetListAdapter(new PlanetListAdapter.PlanetDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        planetViewModel = new ViewModelProvider(this).get(PlanetViewModel.class);
        planetViewModel.getAllPlants().observe(this, planets -> {
            adapter.submitList(planets);
        });

        // Registering RecyclerView for the context menu
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && rv.getLayoutManager() != null) {
                    int position = rv.getLayoutManager().getPosition(child);
                    selectedPosition = position;
                    return false;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNewPlanetActivity.class);
            startActivityForResult(intent, NEW_PLANET_ACTIVITY_REQUEST_CODE);
        });

        updatePlanetLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int planetId = data.getIntExtra(AddNewPlanetActivity.EXTRA_ID, -1);
                            if (planetId != -1) {
                                String planetName = data.getStringExtra(AddNewPlanetActivity.EXTRA_PLANET_NAME);
                                String planetDistance = data.getStringExtra(AddNewPlanetActivity.EXTRA_DISTANCE);
                                String planetDescription = data.getStringExtra(AddNewPlanetActivity.EXTRA_DESCRIPTION);

                                // Update the planet in the list with the new values
                                Planet updatedPlanet = new Planet(planetName, planetDistance, planetDescription);
                                updatedPlanet.setId(planetId);
                                planetViewModel.update(updatedPlanet);

                                Toast.makeText(this, "La planète à été modifier avec succès", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.edit:
                Planet planet = adapter.getPlanetAtPosition(selectedPosition);
                Intent intent = new Intent(MainActivity.this, UpdatePlanetActivity.class);
                intent.putExtra(AddNewPlanetActivity.EXTRA_ID, planet.getId());
                intent.putExtra("planet", planet); // Pass the entire planet object

                startActivityForResult(intent, EDIT_PLANET_REQUEST_CODE);
                //setResult(RESULT_OK, intent);

                return true;
            case R.id.delete:
                final AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                confirm.setTitle("Confirmation!");
                confirm.setMessage("Êtes vous sûr de supprimer cette planète ?");
                confirm.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Planet planetToDelete = adapter.getPlanetAtPosition(selectedPosition);
                        planetViewModel.delete(planetToDelete);
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss(); // Dismiss the dialog
                        Toast.makeText(getApplicationContext(), "La planète à été supprimer", Toast.LENGTH_LONG).show();
                    }
                });
                confirm.setNegativeButton("Annuler", null);
                confirm.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                showSearchDialog();                return true;
            case R.id.help:
                Toast.makeText(getApplicationContext(), "Application PlantsApp réaliser par Intissar Bennouna GLSI 2023-2024", Toast.LENGTH_LONG).show();
                return true;
            case R.id.refresh:
                planetViewModel.getAllPlants().observe(this, planets -> {
                    adapter.submitList(planets);
                });

                // Show a toast message to indicate the refresh
                Toast.makeText(getApplicationContext(), "LA liste à été bien rafraichi", Toast.LENGTH_SHORT).show();                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recherche par nom du planete");

        // Set the custom layout for the dialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_search, null);
        final EditText searchEditText = viewInflated.findViewById(R.id.searchEditText);
        builder.setView(viewInflated);

        // Set the search button
        builder.setPositiveButton(R.string.search_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String searchQuery = searchEditText.getText().toString().trim();
                performSearch(searchQuery);
            }
        });

        // Set the cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void performSearch(String planetName) {
        planetViewModel.searchPlanetsByName(planetName).observe(this, planets -> {
            adapter.submitList(planets);
        });
    }

}
