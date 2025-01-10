package com.titolucas.mindlink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PsychologistAdapter extends RecyclerView.Adapter<PsychologistAdapter.PsychologistViewHolder> {

    private final List<Psychologist> psychologistList;

    public PsychologistAdapter(List<Psychologist> psychologistList) {
        this.psychologistList = psychologistList;
    }

    @NonNull
    @Override
    public PsychologistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_psychologist, parent, false);
        return new PsychologistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PsychologistViewHolder holder, int position) {
        Psychologist psychologist = psychologistList.get(position);
        holder.name.setText(psychologist.getName());
        holder.specialty.setText(psychologist.getSpecialty());
        holder.rating.setText(String.valueOf(psychologist.getRating()));
    }

    @Override
    public int getItemCount() {
        return psychologistList.size();
    }

    public static class PsychologistViewHolder extends RecyclerView.ViewHolder {
        TextView name, specialty, rating;

        public PsychologistViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.psychologist_name);
            specialty = itemView.findViewById(R.id.psychologist_specialty);
            rating = itemView.findViewById(R.id.psychologist_rating);
        }
    }
}
