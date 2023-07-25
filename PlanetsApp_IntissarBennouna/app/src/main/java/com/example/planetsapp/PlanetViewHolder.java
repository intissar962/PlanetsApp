package com.example.planetsapp;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView planetName;
    private final TextView planetDistance;
    private final TextView planetDescription;

    public PlanetViewHolder(@NonNull View itemView) {
        super(itemView);
        planetName = itemView.findViewById(R.id.textViewName);
        planetDistance = itemView.findViewById(R.id.textViewDistance);
        planetDescription = itemView.findViewById(R.id.textViewDescription);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(String name, String distance, String description) {
        planetName.setText(name);
        planetDistance.setText(distance);
        planetDescription.setText(description);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(getAdapterPosition(), R.id.edit, 0, "Modifier");
        menu.add(getAdapterPosition(), R.id.delete, 1, "Supprimer");
    }

    static PlanetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new PlanetViewHolder(view);
    }
}
