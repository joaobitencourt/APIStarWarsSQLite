package com.example.starwars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<Planetas> planetasArrayList;

    public Adapter(ArrayList<Planetas> planetasArrayList) {
        this.planetasArrayList = planetasArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Planetas planetas = planetasArrayList.get(position);
        holder.planete.setText(planetas.getPlanete());
        holder.climate.setText(planetas.getClimate());
        holder.ground.setText(planetas.getGround());
    }

    @Override
    public int getItemCount() {
        return planetasArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
         TextView planete ,climate,ground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            planete = itemView.findViewById(R.id.txtPlanetAdapter);
            climate = itemView.findViewById(R.id.txtClimateAdapter);
            ground = itemView.findViewById(R.id.txtGroundAdapter);
        }
    }

}
