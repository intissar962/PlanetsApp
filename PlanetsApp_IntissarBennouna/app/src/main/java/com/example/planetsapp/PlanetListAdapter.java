package com.example.planetsapp;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.CircularArray;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class PlanetListAdapter extends ListAdapter<Planet, PlanetViewHolder> {
    protected PlanetListAdapter(@NonNull DiffUtil.ItemCallback<Planet> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlanetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PlanetViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetViewHolder holder, int position) {
        Planet current = getItem(position);
        holder.bind(current.getName(), current.getDistance(), current.getDescription());
    }

    public static class PlanetDiff extends DiffUtil.ItemCallback<Planet> {

        @Override
        public boolean areItemsTheSame(@NonNull Planet oldItem, @NonNull Planet newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Planet oldItem, @NonNull Planet newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
    public Planet getPlanetAtPosition (int position) {
        return getItem(position);
    }

}
